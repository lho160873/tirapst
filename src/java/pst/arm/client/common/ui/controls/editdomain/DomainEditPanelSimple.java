package pst.arm.client.common.ui.controls.editdomain;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.domain.EditableDomain;
import pst.arm.client.common.images.CommonImages;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.controls.editdomain.listener.EditStateChangedListener;
import pst.arm.client.common.ui.controls.editdomain.listener.WindowHideListener;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.common.ui.form.EditableForm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public abstract class DomainEditPanelSimple<T extends EditableDomain<T>> extends ContentPanel implements Editable {
    //Abstract methods should be implemented

    public static final EventType Save = new EventType();

    protected abstract GWTEditServiceAsync getService();

    protected abstract Component getEditPanel();

    protected abstract String getHeaderCreate();

    protected String getHeaderViewNull() {
        return "";
    };

    protected abstract String getHeaderViewEdit();

    protected abstract String getHeaderView();

    protected abstract void focusInvalidField();
    //Resources
    protected final CommonConstants constants = GWT.create(CommonConstants.class);
    protected final CommonImages images = GWT.create(CommonImages.class);
    //State and data
    protected T domain=null;
    protected EditState state = EditState.NEW;
    protected EViewType viewType = EViewType.EVT_GRIDEDIT;
    //Controls
    protected Button btnSaveClose, btnCancel, btnSave, btnGetDomainLists,btnDelete,btnAdd,btnEdit;
    protected TextField<String> tfSearch, tfInfo;
    protected ToolBar toolBar;
    protected EditableForm<T> editForms;
    //Listeners
    protected SelectionListener<ButtonEvent> toolBarEventListener;
    protected List<DomainSaveSuccesedListener<T>> domainSaveSuccesedListeners = new ArrayList<DomainSaveSuccesedListener<T>>();
    protected List<EditStateChangedListener> editStateChangedListeners = new ArrayList<EditStateChangedListener>();
    protected List<WindowHideListener> windowHideListeners = new ArrayList<WindowHideListener>();
    protected Boolean isSaveEnabled = Boolean.FALSE;
    protected Boolean isSaveAndCloseEnabled = Boolean.TRUE;
    protected EWindowType windowType = EWindowType.EWT_WINDOW;
    protected Window wnd = null;
    protected LayoutContainer main;
    protected EventBus eventBus;

    public void setEWindowType(EWindowType t) {
        windowType = t;
    }

    public EWindowType getEWindowType() {
        return windowType;
    }

    public void setIsSaveEnabled(Boolean e) {
        isSaveEnabled = e;
        updateControlsVisibility();
    }
    
    public void setIsSaveAndCloseEnabled(Boolean e) {
        isSaveAndCloseEnabled = e;
        updateControlsVisibility();
    }

    public Boolean getIsSaveEnabled() {
        return isSaveEnabled;
    }
        
   
    public void setViewType(EViewType t) {
        viewType = t;
    }

    public EViewType getViewType() {
        return viewType;
    }
    
    protected DomainEditPanelSimple() {
        initListeners();
        initControls();
    }

    public DomainEditPanelSimple(T domain, EditState state) {
        this.domain = domain;
        this.state = state;
        initListeners();
        initControls();
    }

    public DomainEditPanelSimple(T domain, EditState state, EWindowType windowType) {
        this.domain = domain;
        this.state = state;
        this.windowType = windowType;
        initListeners();
        initControls();
    }
    
     public DomainEditPanelSimple(T domain, EditState state,EventBus eventBus) {
        this.domain = domain;
        this.state = state;
        this.eventBus = eventBus;
        initListeners();
        initControls();
    }

    public DomainEditPanelSimple(T domain, EditState state, EWindowType windowType,EventBus eventBus) {
        this.domain = domain;
        this.state = state;
        this.windowType = windowType;
        this.eventBus = eventBus;
        initListeners();
        initControls();
    }
    

    protected void initListeners() {
        toolBarEventListener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String id = ce.getButton().getItemId();
                if (id.equals("cancel")) {                     
                    if (viewType == EViewType.EVT_GRIDEDIT) {
                        closeWindow();
                    } else {
                        updateFormsFromDomain();
                    }
                } else if (id.equals("saveclose")) {
                    if (isValid()) {
                        save(new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                removeAllListeners();
                                hide();
                                if (wnd != null) {
                                    wnd.removeAllListeners();
                                    wnd.hide();
                                }
                                notifyWindowHide();
                            }
                        });
                    }
                } else if (id.equals("save")) {
                    if (isValid()) {
                        save(new Listener() {
                            @Override
                            public void handleEvent(BaseEvent be) {
                                if ( viewType == EViewType.EVT_EDITGRID ) {
                                    setState(EditState.VIEW);
                                    updateControlsVisibility();
                                } else {
                                    setState(EditState.EDIT);
                                    updateControlsVisibility();
                                }
                            }
                        });
                    }
                }
                else if (id.equals("getdomainlists")) {
                    onChangeDomainId();
                    
                }
                else if (id.equals("delete")) {
                    onDelete();                    
                }
                else if (id.equals("edit")) {
                    onEdit();                    
                }
                 else if (id.equals("add")) {
                    onAdd();                    
                }
            }
        };

        addListener(Events.BeforeHide, new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                be.setCancelled(true);
                closeWindow();
            }
        });
    }

    protected void onChangeDomainId() {
    }

    protected void onDelete() {
    }

    protected void onAdd() {
    }

    protected void onEdit() {
    }
    protected void initControls() {
        //setMaximizable(Boolean.TRUE);
        //setClosable(Boolean.TRUE);
        //setPlain(Boolean.TRUE);
        //setModal(Boolean.TRUE);
        setSize(680,580);
        
        setFrame(Boolean.FALSE);
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(true);
        setLayout(new FitLayout());

        main = new LayoutContainer(new BorderLayout());
        main.setBorders(false);
        add(main);
        initToolBar();

    }
    
    protected void initToolBar() {

        toolBar = new ToolBar();
        btnSaveClose = new Button(constants.save(), AbstractImagePrototype.create(images.save()), toolBarEventListener);
        btnSaveClose.setItemId("saveclose");
        btnSave = new Button(constants.save(), AbstractImagePrototype.create(images.save()), toolBarEventListener);
        btnSave.setItemId("save");
        btnCancel = new Button(constants.cancel(), AbstractImagePrototype.create(images.cancel()), toolBarEventListener);
        btnCancel.setItemId("cancel");
        btnGetDomainLists = new Button(constants.choice(), AbstractImagePrototype.create(images.book()), toolBarEventListener);
        btnGetDomainLists.setItemId("getdomainlists");
        btnGetDomainLists.setVisible(Boolean.FALSE);
        btnDelete = new Button(constants.delete(), AbstractImagePrototype.create(images.delete()), toolBarEventListener);
        btnDelete.setItemId("delete");
        btnDelete.setVisible(Boolean.FALSE);
        btnAdd = new Button(constants.add(), AbstractImagePrototype.create(images.add()), toolBarEventListener);
        btnAdd.setItemId("add");
        btnAdd.setVisible(Boolean.FALSE);
        btnEdit = new Button(constants.edit(), AbstractImagePrototype.create(images.edit()), toolBarEventListener);
        btnEdit.setItemId("edit");
        btnEdit.setVisible(Boolean.FALSE);
        
        tfSearch = new  TextFieldSearch();
        tfSearch.setVisible(Boolean.FALSE);
                
        getToolBar().add(btnAdd);
        getToolBar().add(tfSearch);
        getToolBar().add(btnGetDomainLists);
        getToolBar().add(btnSave);
        getToolBar().add(btnSaveClose);
        getToolBar().add(btnEdit);
        getToolBar().add(btnCancel);        
        getToolBar().add(btnDelete);
        
        

        //BorderLayoutData topData = new BorderLayoutData(Style.LayoutRegion.NORTH,100);
        //main.add(toolBar,topData);

        setTopComponent(getToolBar());
    }

    protected void save() {
        save(null);
    }

    protected void save(final Listener actionListener) {
        getEditPanel().mask(constants.saving());
        if (domain == null) {
            domain = createDomain();
        }

        final T oldDomain = domain.newInstance();
        oldDomain.copy(domain);
        editForms.fillDomain(getDomain());
        getService().save(getDomain(), (state == EditState.NEW), new AsyncCallback<T>() {
            @Override
            public void onFailure(Throwable thrwbl) {
                getDomain().copy(oldDomain); //возвращаем старые данные, так как сохранение не прошло
                getEditPanel().unmask();
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(T t) {
                if (t == null) {
                    onFailure(null);
                } else {
                    getDomain().copy(t);
                    updateFormsFromDomain();
                    notifyDomainSaveSuccesed();
                    getEditPanel().unmask();
                    setHeading(getHeaderViewEdit());
                    //if (state.equals(EditState.NEW)) {
                    //    setState(EditState.EDIT);
                    //    updateControlsVisibility();
                    //}
                    if (actionListener != null) {
                        actionListener.handleEvent(new BaseEvent(Save));
                    }
                }
            }
        });
    }

    protected Boolean isValid() {
        if (!editForms.validate()) {
            focusInvalidField();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    protected String getSaveDomainErrorMessage() {
        return "Domain save failed";
    }

    protected String getLoadDomainErrorMessage() {
        return "Get domain full data failed";
    }

    protected void closeWindow() {        
        checkUnsavedChanged(new Listener() {
            @Override
            public void handleEvent(BaseEvent be) {
                removeAllListeners();
                hide();
                if (wnd != null) {
                    wnd.removeAllListeners();
                    wnd.hide();
                }
                notifyWindowHide();
            }
        });
    }

    protected void checkUnsavedChanged(final Listener actionListener) {
        if (state.equals(EditState.VIEW)) {
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
        //setLayout(new FitLayout());
        updateControlsVisibility();
    }

    @Override
    protected void afterRender() {
        super.afterRender();
        initData();
        initHeading();
        /*if (state.equals(EditState.NEW)) {
            setHeading(getHeaderCreate());
        } else if (state.equals(EditState.EDIT)) {
            setHeading(getHeaderViewEdit());
        } else if (state.equals(EditState.VIEW)) {
            setHeading(getHeaderView());
        }*/
    }

    /**
     * Method to update edit controls and content containers by edit state
     */
    protected void updateControlsVisibility() {
        if (getEditPanel().getParent() == main)
            main.remove(getEditPanel());
        if ( viewType == EViewType.EVT_EDITGRID ) {
            //isSaveAndCloseEnabled = Boolean.FALSE;
             isSaveEnabled = Boolean.TRUE;
             isSaveAndCloseEnabled= Boolean.FALSE;
        }
        switch (state) {
            case NEW:
                btnCancel.setText(constants.cancel());
                //btnSaveClose.show();
                btnCancel.show();
                updateBtnSaveInfo();
                main.add(getEditPanel(), new BorderLayoutData(Style.LayoutRegion.CENTER));
                break;
            case EDIT:
                btnCancel.setText(constants.cancel());
                //btnSaveClose.show();
                btnCancel.show();
                updateBtnSaveInfo();
                main.add(getEditPanel(), new BorderLayoutData(Style.LayoutRegion.CENTER));
                break;
            case VIEW:
                btnCancel.setText(constants.exit());
                btnSaveClose.hide();
                btnSave.hide();
                btnCancel.show();
                main.add(getEditPanel(), new BorderLayoutData(Style.LayoutRegion.CENTER));
                break;
        }
        //updateBtnSaveInfo();
        btnGetDomainLists.setVisible(viewType == EViewType.EVT_EDITGRID);    
        btnDelete.setVisible(viewType == EViewType.EVT_EDITGRID);
        btnAdd.setVisible(viewType == EViewType.EVT_EDITGRID);
        btnEdit.setVisible(viewType == EViewType.EVT_EDITGRID);   
        tfSearch.setVisible(viewType == EViewType.EVT_EDITGRID);   
        if (viewType == EViewType.EVT_EDITGRID)
        {
            btnCancel.setText(constants.cancel());
        } 
        enabledBtn(); //доступность кнопок - провеки прав доступа
        layout();
    }
    
    //доступность кнопок - провеки прав доступа
    public void enabledBtn() {     
        btnSave.setEnabled( !state.equals(EditState.VIEW ));
        btnSaveClose.setEnabled( !state.equals(EditState.VIEW ));        
        btnEdit.setEnabled( !state.equals(EditState.VIEW ));
        btnDelete.setEnabled( !state.equals(EditState.VIEW) ); 
        if (viewType == EViewType.EVT_EDITGRID) {
            btnSave.show();
            btnSave.setEnabled(state != EditState.VIEW);
            btnSaveClose.setEnabled(state != EditState.VIEW);
            btnCancel.setEnabled(state != EditState.VIEW);
            btnEdit.setEnabled(state == EditState.VIEW);
            btnDelete.setEnabled(state == EditState.VIEW);
        }
    }
    
    protected void updateBtnSaveInfo() {
        if (isSaveEnabled && isSaveAndCloseEnabled) {
            btnSaveClose.setText(constants.saveclose());
            btnSaveClose.setIcon(AbstractImagePrototype.create(images.saveClose()));
            btnSave.setText(constants.save());
            btnSave.setIcon(AbstractImagePrototype.create(images.save()));
            btnSave.show();
            btnSaveClose.show();
        }else if ( !isSaveEnabled && isSaveAndCloseEnabled) {
            btnSaveClose.setText(constants.save());
            btnSaveClose.setIcon(AbstractImagePrototype.create(images.save()));
            btnSave.hide();   
            btnSaveClose.show();
        }else if ( isSaveEnabled && !isSaveAndCloseEnabled) {
            btnSave.setText(constants.save());
            btnSave.setIcon(AbstractImagePrototype.create(images.save()));
            btnSave.show();
            btnSaveClose.hide();
        }
    }

    protected void registerEditForm(EditableForm<T> form) {
        editForms = form;
    }

    public void addWindowHideListener(WindowHideListener listener) {
        windowHideListeners.add(listener);
    }

    protected void notifyWindowHide() {
        for (WindowHideListener listener : windowHideListeners) {
            listener.onWindowHide();
        }
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

    protected void updateFormsFromDomain() {
        if (domain != null) {
            editForms.setDomain(domain);
        }

    }

    protected Boolean hasUnsavedChanges() {
        T empty = (domain != null) ? domain.newInstance() : createDomain();
        if (empty == null) {
            return false;
        }
        editForms.fillDomain(empty);
        return (!domain.isDomainEquals(empty));
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
     * @return the toolBar
     */
    public ToolBar getToolBar() {
        return toolBar;
    }

    /**
     * здесь может быть дополнительная загрузка данных
     */
    protected void initData() {
        updateFormsFromDomain();
    }

    @Override
    public void show() {
        if (windowType == EWindowType.EWT_WINDOW) {
            wnd = new Window();
            wnd.setMaximizable(Boolean.TRUE);
            wnd.setClosable(Boolean.TRUE);
            wnd.setPlain(Boolean.TRUE);
            wnd.setModal(Boolean.TRUE);
            wnd.setLayout(new FitLayout());
            this.setHeaderVisible(false);
            this.setBorders(false);
            wnd.add(this);
            wnd.setModal(true);
            wnd.setSize(this.width,this.height);            
            initHeading();
            wnd.addListener(Events.BeforeHide, new Listener() {
                @Override
                public void handleEvent(BaseEvent be) {
                    be.setCancelled(true);
                    closeWindow();
                }
            });


            wnd.show();
        } else {
            super.show();
        }
    }
    
    protected void initHeading() {
        if (wnd != null) {
            if (state.equals(EditState.NEW)) {
                wnd.setHeading(getHeaderCreate());
            } else if (state.equals(EditState.EDIT)) {
                wnd.setHeading(getHeaderViewEdit());
            } else if (state.equals(EditState.VIEW)) {
                wnd.setHeading(getHeaderView());
            }
        } else {
            if (state.equals(EditState.NEW)) {
                this.setHeading(getHeaderCreate());
            } else if (state.equals(EditState.EDIT)) {
                this.setHeading(getHeaderViewEdit());
            } else if (state.equals(EditState.VIEW)) {
                this.setHeading(getHeaderView());
            }
        }
    }
    
    protected void onSearch()
    {
        
    }
    
    protected class TextFieldSearch extends TextField<String> {

        @Override
        protected void onKeyPress(FieldEvent fe) {
            super.onKeyPress(fe);
            if (fe.getKeyCode() == KeyCodes.KEY_ENTER) {
                onSearch();
            }
        }
        
    };
    
    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
