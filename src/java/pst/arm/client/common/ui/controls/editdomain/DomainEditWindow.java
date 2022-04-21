package pst.arm.client.common.ui.controls.editdomain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.domain.EditableDomain;
import pst.arm.client.common.domain.search.SearchCondition;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.controls.editdomain.listener.EditStateChangedListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.EditableForm;

/**
 * Base generic abstract component-window for view/edit any domain objects
 * Domain must implement EditableDomain interface
 *
 * @author Alexandr Kozhin
 * @since 0.14.0
 * @see pst.arm.client.common.domain.EditableDomain
 * @see pst.arm.client.common.ui.Editable
 */
public abstract class DomainEditWindow<T extends EditableDomain<T>> extends Window implements Editable {
    //Abstract methods should be implemented

    public static final EventType Save = new EventType();
    
    protected abstract GWTEditServiceAsync getService();
    
    protected abstract Component getEditPanel();
    
    protected abstract Component getViewPanel();
    
    protected abstract String getHeaderCreate();
    
    protected abstract String getHeaderViewEdit();
    
    protected abstract void focusInvalidField();
    //Resources
    protected final CommonConstants constants = GWT.create(CommonConstants.class);
    protected final CommonImages images = GWT.create(CommonImages.class);
    //State and data
    protected T domain;
    protected EditState state = EditState.NEW;
    protected EditMode mode = EditMode.VIEWEDIT;
    protected Boolean needMaximize = Boolean.FALSE;
    protected Boolean copyEnabled = Boolean.FALSE;
    private Boolean addEnabled = Boolean.FALSE;
    private SearchCondition condition;
    //Controls
    protected Button btnAdd, btnView, btnEdit, btnCopy, btnSave, btnSaveClose, btnCancel;
    private ToolBar toolBar;
    protected SimplePageToolBar pagingToolBar;
    protected List<EditableForm<T>> editForms = new ArrayList<EditableForm<T>>();
    protected Grid<BeanModel> grid;
    //Listeners
    protected SelectionListener<ButtonEvent> toolBarEventListener;
    protected List<DomainSaveSuccesedListener<T>> domainSaveSuccesedListeners = new ArrayList<DomainSaveSuccesedListener<T>>();
    protected List<EditStateChangedListener> editStateChangedListeners = new ArrayList<EditStateChangedListener>();
    private boolean isReadyForPageChange = false;

    /**
     * Base constructor is private
     */
    private DomainEditWindow() {
        initListeners();
        initControls();
        initCondition();
    }

    /**
     * First constructor for create domain mode
     *
     * @param domain New domain object
     */
    public DomainEditWindow(T domain) {
        this();
        this.domain = domain;
    }

    /**
     * Second constructor for view/edit mode with link to grid
     *
     * @param domain Domain object to view/edit
     * @param grid Link to grid
     */
    public DomainEditWindow(T domain, EditState state, Grid<BeanModel> grid) {
        this.domain = domain;
        this.grid = grid;
        this.state = state;
        initListeners();
        initControls();
    }
    
    public DomainEditWindow(T domain, Grid<BeanModel> grid) {
        this(domain, EditState.EDIT, grid);
    }
    
    private void initListeners() {
        toolBarEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String id = ce.getButton().getItemId();
                if (id.equals("add")) {
                    checkUnsavedChanged(new Listener() {
                        @Override
                        public void handleEvent(BaseEvent be) {
                            create();
                        }
                    });
                } else if (id.equals("copy")) {
                    checkUnsavedChanged(new Listener() {
                        @Override
                        public void handleEvent(BaseEvent be) {
                            T oldDomain = getDomain();
                            create();
                            copy(oldDomain);
                        }
                    });
                } else if (id.equals("view")) {
                    setState(EditState.VIEW);
                    updateControlsVisibility();
                } else if (id.equals("edit")) {
                    setState(EditState.EDIT);
                    updateControlsVisibility();
                } else if (id.equals("save")) {
                    if (isValid()) {
                        save();
                    }
                } else if (id.equals("cancel")) {
                    if (domain == null || domain.getDomainId() == null) {
                        clearForms();
                    } else {
                        for (EditableForm<T> form : editForms) {
                            form.setDomain(domain);
                        }
                    }
                } else if (id.equals("saveclose")) {
                    if (isValid()) {
                        save(new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                removeAllListeners();
                                hide();
                            }
                        });
                    }
                }
            }
            
            private void create() {
                domain = createDomain();
                
                for (EditableForm<T> form : editForms) {
                    form.setDomain(domain);
                }
                setState(EditState.NEW);
                updateControlsVisibility();
                setHeading(getHeaderCreate());
            }
        };
        
        addListener(Events.BeforeHide, new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                be.setCancelled(true);
                closeWindow();
            }
        });
        
        addListener(Events.KeyUp, new KeyListener() {
            @Override
            public void componentKeyUp(ComponentEvent event) {
                switch (event.getKeyCode()) {
                    case KeyCodes.KEY_ESCAPE:
                        onEscape();
                        break;
                }
            }
        });
    }
    
    private void initControls() {
        setMaximizable(Boolean.TRUE);
        setClosable(Boolean.TRUE);
        setPlain(Boolean.TRUE);
        setModal(Boolean.TRUE);
        setFrame(Boolean.TRUE);
        initToolBar();
        initPagingToolBar();
    }
    
    protected void initCondition() {
        if (condition == null) {
            condition = new SearchCondition();
        }
    }
    
    private void initToolBar() {
        toolBar = new ToolBar();
        btnView = new Button(constants.view(), AbstractImagePrototype.create(images.view()), toolBarEventListener);
        btnView.setItemId("view");
        btnAdd = new Button(constants.add(), AbstractImagePrototype.create(images.add()), toolBarEventListener);
        btnAdd.setItemId("add");
        btnEdit = new Button(constants.edit(), AbstractImagePrototype.create(images.edit()), toolBarEventListener);
        btnEdit.setItemId("edit");
        btnSave = new Button(constants.save(), AbstractImagePrototype.create(images.save()), toolBarEventListener);
        btnSave.setItemId("save");
        btnCancel = new Button(constants.cancel(), AbstractImagePrototype.create(images.cancel()), toolBarEventListener);
        btnCancel.setItemId("cancel");
        btnCopy = new Button(constants.copy(), AbstractImagePrototype.create(images.copy()), toolBarEventListener);
        btnCopy.setItemId("copy");
        btnCopy.hide();
        btnSaveClose = new Button(constants.saveclose(), AbstractImagePrototype.create(images.saveclose()), toolBarEventListener);
        btnSaveClose.setItemId("saveclose");
        getToolBar().add(btnView);
        if (ConfigurationManager.getPropertyAsBoolean("controls.add.visible")) {
            getToolBar().add(btnAdd);
            getToolBar().add(btnCopy);
        }
        if (ConfigurationManager.getPropertyAsBoolean("controls.edit.visible")) {
            getToolBar().add(btnEdit);
            getToolBar().add(btnSave);
            getToolBar().add(btnSaveClose);
        }
        
        getToolBar().add(btnCancel);
        setTopComponent(getToolBar());
    }
    
    protected void initPagingToolBar() {
        pagingToolBar = new SimplePageToolBar(grid, Boolean.TRUE) {
            @Override
            public boolean isReadyForPageChange(final int currentPage) {
                isReadyForPageChange = false;
                checkUnsavedChanged(new Listener() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        if (be != null) {
                            // событие срабатывает позже чем завершается функция checkUnsavedChanged
                            // соотвесвенно перелистываем страницу "вручную"
                            setCurrentPage(currentPage);
                        } else {
                            // событие срабатывает раньше чем завершается функция checkUnsavedChanged
                            isReadyForPageChange = true;
                        }
                    }
                });
                return isReadyForPageChange;
            }
            
            @Override
            public void onPageChanged() {
                domain = (T) getCurrentModel().getBean();
                updateMode();
                updateControlsVisibility();
                //updateFormsFromDomain();
                initData();//LKHorosheva
                setHeading(getHeaderViewEdit());
            }
        };
        pagingToolBar.setBorders(false);
        setBottomComponent(pagingToolBar);
    }
    
    public void hidePaging() {
        pagingToolBar.setVisible(Boolean.TRUE);
    }
    
    protected void initData() {
        if (!getDomain().isDomainFull()) {
            getVisiblePanel().mask(constants.loading());
            pagingToolBar.mask();
            getService().getDomainById(domain.getDomainId(), new AsyncCallback<T>() {
                @Override
                public void onFailure(Throwable thrwbl) {
                    MessageBox.alert(constants.error(), getLoadDomainErrorMessage(), null);
                    getVisiblePanel().unmask();
                    pagingToolBar.unmask();
                }
                
                @Override
                public void onSuccess(T t) {
                    getDomain().copy(t);
                    updateFormsFromDomain();
                    setHeading(getHeaderViewEdit());
                    getVisiblePanel().unmask();
                    pagingToolBar.unmask();
                }
            });
        } else {
            updateFormsFromDomain();
        }
    }
    
    protected void save() {
        save(null);
    }
    
    protected void save(final Listener actionListener) {
        getEditPanel().mask(constants.saving());
        if (domain == null) {
            domain = createDomain();
        }
        for (EditableForm<T> form : editForms) {
            form.fillDomain(getDomain());
        }
        getService().save(getDomain(), (state == EditState.NEW), new AsyncCallback<T>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                getEditPanel().unmask();
                MessageBox.alert(constants.error(), getSaveDomainErrorMessage(), null);
            }
            
            @Override
            public void onSuccess(T t) {
                if (t == null) {
                    onFailure(null);
                } else {
                    getDomain().copy(t);
                    updateGridFromDomain();
                    //updateFormsFromDomain();
                    initData();//LKHorosheva
                    notifyDomainSaveSuccesed();
                    getEditPanel().unmask();
                    setHeading(getHeaderViewEdit());
                    if (state.equals(EditState.NEW)) {
                        state = EditState.EDIT;
                        updateControlsVisibility();
                    }
                    if (actionListener != null) {
                        actionListener.handleEvent(new BaseEvent(Save));
                    }
                }
            }
        });
    }
    
    protected Boolean isValid() {
        for (EditableForm<T> editableForm : editForms) {
            if (!editableForm.validate()) {
                focusInvalidField();
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    
    protected String getSaveDomainErrorMessage() {
        return "Domain save faild";
    }
    
    protected String getLoadDomainErrorMessage() {
        return "Get domain full data faild";
    }
    
    protected void closeWindow() {
        checkUnsavedChanged(new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                removeAllListeners();
                hide();
            }
        });
    }
    
    protected void checkUnsavedChanged(final Listener actionListener) {
        if (mode.equals(EditMode.VIEWONLY) || state.equals(EditState.VIEW)) {
            actionListener.handleEvent(null);
        } else {
            if (!hasUnsavedChanges()) {
                actionListener.handleEvent(null);
                
            } else {
                MessageBox box = new MessageBox();
                box.setButtons(MessageBox.YESNOCANCEL);
                box.setIcon(MessageBox.QUESTION);
                box.setTitle(constants.save());
                box.addCallback(new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(final MessageBoxEvent be) {
                        
                        if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                            if (isValid()) {
                                save(actionListener);
                            }
                        } else if (be.getButtonClicked().getItemId().equals(Dialog.NO)) {
                            actionListener.handleEvent(be);
                        }
                    }
                });
                box.setMessage(constants.saveChanges());
                box.show();
            }
        }
    }
    
    @Override
    public void setState(EditState state) {
        this.state = state;
        notifyEditStateChanged();
    }
    
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setLayout(new FitLayout());
        updateControlsVisibility();
    }
    
    @Override
    protected void afterRender() {
        super.afterRender();
        initData();
        if (!state.equals(EditState.NEW)) {
            setHeading(getHeaderViewEdit());
        } else {
            setHeading(getHeaderCreate());
        }
    }
    
    @Override
    public void hide() {
        super.hide();
        pagingToolBar.setCursorToDefaultPos();
    }

    /**
     * Method to update edit controls and content containers by edit state
     */
    protected void updateControlsVisibility() {
        removeAll();
        switch (mode) {
            case VIEWONLY:
                state = EditState.VIEW;
                getToolBar().hide();
                break;
            
            case EDITONLY:
                btnView.hide();
                btnEdit.hide();
                break;
            
            case VIEWEDIT:
                getToolBar().show();
                break;
        }
        switch (state) {
            case NEW:
                pagingToolBar.hide();
                btnView.hide();
                btnSave.show();
                btnSaveClose.show();
                btnCancel.show();
                btnAdd.hide();
                btnCopy.hide();
                if (!mode.equals(EditMode.EDITONLY)) {
                    btnEdit.hide();
                }
                add(getEditPanel());
                break;
            case VIEW:
                btnView.hide();
                btnSave.hide();
                btnSaveClose.hide();
                btnCopy.hide();
                btnCancel.hide();
                btnEdit.show();
                btnAdd.hide();
                add(getViewPanel());
                break;
            case EDIT:
                btnSave.show();
                btnSaveClose.show();
                if (isCopyEnabled()) {
                    btnCopy.show();
                }
                if (isAddEnabled()) {
                    btnAdd.show();
                }
                btnCancel.show();
                if (!mode.equals(EditMode.EDITONLY)) {
                    btnEdit.hide();
                    btnView.show();
                }
                add(getEditPanel());
                break;
        }
        layout();
    }
    
    protected void registerEditForm(EditableForm<T> form) {
        editForms.add(form);
    }
    
    public void addDomainSaveSuccesedListener(DomainSaveSuccesedListener<T> listener) {
        domainSaveSuccesedListeners.add(listener);
    }
    
    protected void notifyDomainSaveSuccesed() {
        for (DomainSaveSuccesedListener<T> listener : domainSaveSuccesedListeners) {
            listener.onDomainSaveSucceed(getDomain());
        }
    }
    
    public void addEditStateChangedListener(EditStateChangedListener listener) {
        editStateChangedListeners.add(listener);
    }
    
    protected void notifyEditStateChanged() {
        for (EditStateChangedListener listener : editStateChangedListeners) {
            listener.onEditStatusChanges(state);
        }
    }
    
    protected void updateGridFromDomain() {
        if (pagingToolBar.isVisible()) {
            grid.getStore().update(grid.getStore().getAt(pagingToolBar.getCurrentPage()));
        }
    }
    
    protected void updateFormsFromDomain() {
        if (domain != null && domain.isDomainFull()) {
            for (EditableForm<T> form : editForms) {
                form.setDomain(domain);
            }
        } //LKHorosheva
        //else {
        //    initData();
        //}
    }
    
    protected Boolean hasUnsavedChanges() {
        T empty = (domain != null) ? domain.newInstance() : createDomain();
        
        if (empty == null) {
            return false;
        }
        
        for (EditableForm<T> form : editForms) {
            form.fillDomain(empty);
        }
        return (!domain.isDomainEquals(empty));
    }
    
    public Component getVisiblePanel() {
        return (state.equals(EditState.VIEW)) ? getViewPanel() : getEditPanel();
    }
    
    protected void copy(T oldDomain) {
        throw new UnsupportedOperationException("Copy domain method not implemented");
    }
    
    protected void onEscape() {
        closeWindow();
    }

    /**
     * @return the mode
     */
    public EditMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(EditMode mode) {
        this.mode = mode;
    }

    /**
     * @return the domain
     */
    public T getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(T domain) {
        this.domain = domain;
    }
    
    protected T createDomain() {
        if (domain != null) {
            return domain.newInstance();
        }
        return null;
    }

    /**
     * @return the condition
     */
    public SearchCondition getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(SearchCondition condition) {
        this.condition = condition;
    }

    /**
     * @return the toolBar
     */
    public ToolBar getToolBar() {
        return toolBar;
    }

    /**
     * Calls before update form from new domain Implements this method if need
     * change mode by condition
     */
    protected void updateMode() {
        return;
    }
    
    private boolean isCopyEnabled() {
        return this.copyEnabled;
    }
    
    public void setCopyEnabled(Boolean copyEnabled) {
        this.copyEnabled = copyEnabled;
    }
    
    private void clearForms() {
        for (EditableForm<T> form : editForms) {
            form.clear();
        }
    }

    /**
     * @return the addEnabled
     */
    public Boolean isAddEnabled() {
        return addEnabled;
    }

    /**
     * @param addEnabled the addEnabled to set
     */
    public void setAddEnabled(Boolean addEnabled) {
        this.addEnabled = addEnabled;
    }
}
