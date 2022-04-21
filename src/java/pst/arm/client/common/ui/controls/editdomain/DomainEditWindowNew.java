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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.AppHelper;
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
 * Базовый класс окна предназанченного для создания/редактированя/просмотра
 * любого домена (указанного как параметр шаблона класса). Домен - это объект
 * имплиментирующий интерфейс EditableDomain.
 *
 * Окно представляется в одном из трёх режимов: создание редактировавние
 * просмотр пееключение между режимами осуществляется с помощью панели
 * инструментов. Возможность переключения может быть ограничена. Т.е. окно можно
 * сконфигурировать так что оно будет открываться только в определённых режимах.
 * Конфигурирование окна осуществляется с помощью класса DomainEditWindowConfig
 *
 *
 * @author Alexandr Kozhin
 * @since 0.14.0
 * @see ru.spb.iac.archives.arm.client.common.domain.EditableDomain
 * @see ru.spb.iac.archives.arm.client.common.ui.Editable
 */
public abstract class DomainEditWindowNew<T extends EditableDomain<T>> extends Window implements Editable {
    //Abstract methods should be implemented

    public static final EventType Save = new EventType();

    protected abstract GWTEditServiceAsync getService();

    protected abstract Component getEditPanel();

    protected abstract Component getViewPanel();

    protected abstract String getHeaderCreate();

    protected abstract String getHeaderViewEdit();

    protected abstract void focusInvalidField();
    //Resources
    protected final CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected final CommonImages images = GWT.create(CommonImages.class);
    //State and data
    private T domain;
    private DomainEditWindowConfig config;
    protected Boolean needMaximize = Boolean.FALSE;
    private SearchCondition condition;
    //Controls
    protected Button btnAdd, btnView, btnEdit, btnCopy, btnSave, btnSaveClose, btnSaveAdd, btnCancel;
    private ToolBar toolBar;
    /**
     * тулбар от грида, для листания элеменов/доменов в режиме просмотра
     */
    protected SimplePageToolBar pagingToolBar;
    /**
     * editForms - список форм, где форма это котнрол отображающий часть данных
     * домена или весь домен. В список входят все контролы и для редактирования
     * и для просмотра. Добавлять контролы в список (регестрировать с помощью
     * функции registerEditForm ) требуется для поддержки следующего
     * функционала: для изменения содержимого этих самых контролов при
     * перелистовании pagingToolBar'а или смене режима (EDIT -> VIEW), и
     * производится это с помощью интерфейса setDomain
     */
    protected List<EditableForm<T>> editForms = new ArrayList<EditableForm<T>>();
    protected Grid<BeanModel> grid;
    //Listeners
    protected SelectionListener<ButtonEvent> toolBarEventListener;
    /**
     * список слушателей собития о завершении удачного сохранения домена.
     * оповещение (вызов метода onDomainSaveSucceed) происходит после обновления
     * контролов, но до снятия маски
     */
    protected List<DomainSaveSuccesedListener<T>> domainSaveSuccesedListeners = new ArrayList<DomainSaveSuccesedListener<T>>();
    /**
     * список слушателей собития о смене режима окна. оповещение (вызов метода
     * onEditStatusChanges) до обновления/смены контролов
     */
    protected List<EditStateChangedListener> editStateChangedListeners = new ArrayList<EditStateChangedListener>();
    private boolean isReadyForPageChange = false;

    /**
     * First constructor for create domain mode
     *
     * @param domain New domain object
     */
    public DomainEditWindowNew(T domain, DomainEditWindowConfig config) {
        initListeners();
        initControls();
        initCondition();
        this.domain = domain;
        this.config = config;
    }

    /**
     * Second constructor for view/edit mode with link to grid
     *
     * @param domain Domain object to view/edit
     * @param grid Link to grid
     */
    public DomainEditWindowNew(T domain, DomainEditWindowConfig config, Grid<BeanModel> grid) {
        this.domain = domain;
        this.grid = grid;
        this.config = config;
        initListeners();
        initControls();
        updateModes();
    }

    public DomainEditWindowNew(T domain, EditState state, Grid<BeanModel> grid) {
        this(domain, new DomainEditWindowConfig(state), grid);
        getConfig().setState(state);
    }
    
    public void setGrid(Grid<BeanModel> grid)
    {
        this.grid = grid;
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
                    checkUnsavedChanged(new Listener() {

                        @Override
                        public void handleEvent(BaseEvent be) {
                            setState(EditState.VIEW);
                            setCurrentPageDomain();
                            updateControlsVisibility();
                        }
                    });
                } else if (id.equals("edit")) {
                    config.enabledViewMode(true);
                    setState(EditState.EDIT);
                    updateControlsVisibility();
                } else if (id.equals("save")) {
                    if (isValid()) {
                        save(new Listener() {

                            @Override
                            public void handleEvent(BaseEvent be) {
                                setState(EditState.EDIT);
                                updateControlsVisibility();
                            }
                        });
                    }
                } else if (id.equals("saveadd")) {
                    if (isValid()) {
                        save(new Listener() {

                            @Override
                            public void handleEvent(BaseEvent be) {
                                create();
                            }
                        });
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
                } else if (id.equals("cancel")) {
                    if (domain == null || domain.getDomainId() == null) {
                        clearForms();
                    } else {
                        for (EditableForm<T> form : editForms) {
                            form.setDomain(domain);
                        }
                        isValid();
                    }
                }
            }

            private void create() {
                grid = null;
                config.enabledEditMode(true);
                domain = createDomain();
                for (EditableForm<T> form : editForms) {
                    form.setDomain(domain);
                }
                setState(EditState.NEW);
                updateControlsVisibility();
//                setHeading(getHeaderCreate());
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

    protected void initToolBar() {
        toolBar = new ToolBar();
        btnView = new Button(commonConstants.view(), AbstractImagePrototype.create(images.view()), toolBarEventListener);
        btnView.setItemId("view");
        btnAdd = new Button(commonConstants.add(), AbstractImagePrototype.create(images.add()), toolBarEventListener);
        btnAdd.setItemId("add");
        btnEdit = new Button(commonConstants.edit(), AbstractImagePrototype.create(images.edit()), toolBarEventListener);
        btnEdit.setItemId("edit");
        btnSave = new Button(commonConstants.save(), AbstractImagePrototype.create(images.save()), toolBarEventListener);
        btnSave.setItemId("save");
        btnCancel = new Button(commonConstants.cancel(), AbstractImagePrototype.create(images.cancel()), toolBarEventListener);
        btnCancel.setItemId("cancel");
        btnCopy = new Button(commonConstants.copy(), AbstractImagePrototype.create(images.copy()), toolBarEventListener);
        btnCopy.setItemId("copy");
        btnSaveAdd = new Button(commonConstants.saveadd(), AbstractImagePrototype.create(images.saveNext()), toolBarEventListener);
        btnSaveAdd.setItemId("saveadd");
        btnSaveClose = new Button(commonConstants.saveclose(), AbstractImagePrototype.create(images.saveClose()), toolBarEventListener);
        btnSaveClose.setItemId("saveclose");
        getToolBar().add(btnView);
        getToolBar().add(btnAdd);
        getToolBar().add(btnCopy);
        getToolBar().add(btnEdit);
        getToolBar().add(btnSave);
        getToolBar().add(btnSaveAdd);
        getToolBar().add(btnSaveClose);

        getToolBar().add(btnCancel);
        setTopComponent(getToolBar());
    }

    protected void initPagingToolBar() {
        if (grid == null) {
            pagingToolBar = null;
            return;
        }
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
                setCurrentPageDomain();
                updateControlsVisibility();
//                domain = (T) getCurrentModel().getBean();
//                updateModes();
//                updateControlsVisibility();
//                updateFormsFromDomain();
//                setHeading(getHeaderViewEdit());
            }
        };
        pagingToolBar.setBorders(false);
        setBottomComponent(pagingToolBar);
    }

    private void setCurrentPageDomain() {
        if (grid != null) {
            domain = (T) pagingToolBar.getCurrentModel().getBean();
            updateModes();
            updateFormsFromDomain();
        }
    }

    public void hidePaging() {
        if (pagingToolBar != null) {
            pagingToolBar.setVisible(Boolean.TRUE);
        }
    }

    protected void initData() {
        if (!getDomain().isDomainFull()) {
            getVisiblePanel().mask(commonConstants.loading());
            if (pagingToolBar != null) {
                pagingToolBar.mask();
            }
            getService().getDomainById(domain.getDomainId(), new AsyncCallback<T>() {

                @Override
                public void onFailure(Throwable thrwbl) {
                    MessageBox.alert(commonConstants.error(), getLoadDomainErrorMessage(), null);
                    getVisiblePanel().unmask();
                    if (pagingToolBar != null) {
                        pagingToolBar.unmask();
                    }
                }

                @Override
                public void onSuccess(T t) {
                    getDomain().copy(t);
                    updateFormsFromDomain();
//                    setHeading(getHeaderViewEdit());
                    getVisiblePanel().unmask();
                    if (pagingToolBar != null) {
                        pagingToolBar.unmask();
                    }
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
        getEditPanel().mask(commonConstants.saving());
        if (domain == null) {
            domain = createDomain();
        }
        for (EditableForm<T> form : editForms) {
            form.fillDomain(getDomain());
        }
        getService().save(getDomain(), (getConfig().getState() == EditState.NEW), new AsyncCallback<T>() {

            @Override
            public void onFailure(Throwable thrwbl) {
                getEditPanel().unmask();
                //MessageBox.alert(commonConstants.error(), getSaveDomainErrorMessage(), null);
                //MessageBox.alert(commonConstants.error(), thrwbl.getMessage(), null);
                AppHelper.showMsgRpcServiceError(thrwbl);
            }

            @Override
            public void onSuccess(T t) {
                if (t == null) {
                    onFailure(null);
                } else {
                    getDomain().copy(t);
                    notifyDomainSaveSuccesed();
                    if ((getConfig().getState() != EditState.NEW)) {
                        updateGridFromDomain();
                    } else {
                        updateGridFromDomainNew();
                    }
                    updateFormsFromDomain();
                    getEditPanel().unmask();
//                    setHeading(getHeaderViewEdit());
//                    if (config.getState().equals(EditState.NEW)) {
//                        config.setState(EditState.EDIT);
//                        updateControlsVisibility();
//                    }
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
        if ((!config.standByEditMode() && !config.standByNewMode()) || config.getState().equals(EditState.VIEW)) {
            actionListener.handleEvent(null);
        } else {
            if (!hasUnsavedChanges()) {
                actionListener.handleEvent(null);
            } else {
                MessageBox box = new MessageBox();
                box.setButtons(MessageBox.YESNOCANCEL);
                box.setIcon(MessageBox.QUESTION);
                box.setTitle(commonConstants.save());
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
                box.setMessage(commonConstants.saveChanges());
                box.show();
            }
        }
    }

    @Override
    public void setState(EditState state) {
        config.setState(state);
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
        if (!config.getState().equals(EditState.NEW)) {
            initData();
            setHeading(getHeaderViewEdit());
        } else {
            setHeading(getHeaderCreate());
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (pagingToolBar != null) {
            pagingToolBar.setCursorToDefaultPos();
        }
    }

    /**
     * Method to update edit controls and content containers by edit state
     */
    protected void updateControlsVisibility() {
        removeAll();
        if (config.standByMode(config.getState())) {
            btnView.hide();
            btnEdit.hide();
            btnAdd.hide();
            btnCopy.hide();
            btnSave.hide();
            btnSaveAdd.hide();
            btnSaveClose.hide();
            btnCancel.hide();

            switch (config.getState()) {
                case VIEW:
                    if (grid != null) {
                        pagingToolBar.show();
                    }
                    if (config.standByEditMode()) {
                        btnEdit.show();
                    }
                    if (config.standByNewMode()) {
                        btnAdd.show();
                        if (config.isCopyEnabled()) {
                            btnCopy.show();
                        }
                        if (config.isSaveAddEnabled()) {
                            btnSaveAdd.show();
                        }
                    }
                    add(getViewPanel());
                    setHeading(getHeaderViewEdit());
                    break;
                case EDIT:
                    if (pagingToolBar != null) {
                        pagingToolBar.hide();
                    }
                    btnSave.show();
                    btnSaveClose.show();
                    btnCancel.show();
                    if (config.standByViewMode()) {
                        btnView.show();
                    }
                    add(getEditPanel());
                    setHeading(getHeaderViewEdit());
                    break;
                case NEW:
                    if (pagingToolBar != null) {
                        pagingToolBar.hide();
                    }
                    if (config.standByEditMode()) {
                        btnSave.show();
                    }
                    if (config.isSaveAddEnabled()) {
                        btnSaveAdd.show();
                    }
                    btnSaveClose.show();
                    btnCancel.show();
                    add(getEditPanel());
                    setHeading(getHeaderCreate());
                    break;
            }
            layout();
            //  getToolBar().repaint();
        } else {
            MessageBox.alert(commonConstants.error(), "попытка открыть окно редактирования в недоступном режиме", null);
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    protected void registerEditForm(EditableForm<T> form) {
        editForms.add(form);
    }

    protected void unregisterEditForm(EditableForm<T> form) {
        editForms.remove(form);
    }

    public void addDomainSaveSuccesedListener(DomainSaveSuccesedListener<T> listener) {
        domainSaveSuccesedListeners.add(listener);
    }

    public void removeDomainSaveSuccesedListener(DomainSaveSuccesedListener<T> listener) {
        domainSaveSuccesedListeners.remove(listener);
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
            listener.onEditStatusChanges(config.getState());
        }
    }

    protected void updateGridFromDomain() {
        if (grid != null && grid.getStore() != null && pagingToolBar.getCurrentPage() >= 0) {
            grid.getStore().update(grid.getStore().getAt(pagingToolBar.getCurrentPage()));
        }
    }

    protected void updateGridFromDomainNew() {
    }
    
    protected void updateFormsFromDomain() {
        if (domain != null && domain.isDomainFull()) {
            for (EditableForm<T> form : editForms) {
                form.setDomain(domain);
                switch (config.getState()) {
                    case VIEW:
                    case EDIT:
                        setHeading(getHeaderViewEdit());
                        break;
                    case NEW:
                        setHeading(getHeaderCreate());
                        break;
                    default:
                        break;
                }
            }
        } else {
            initData();
        }
    }

    protected Boolean hasUnsavedChanges() {
        T empty = (domain != null) ? domain.newInstance() : createDomain();

        if (empty == null) {
            return false;
        }

        for (EditableForm<T> form : editForms) {
            form.fillDomain(empty);
        }
        return !domain.isDomainEquals(empty);
    }

    
    public Component getVisiblePanel() {
        return (config.getState().equals(EditState.VIEW)) ? getViewPanel() : getEditPanel();
    }

    protected void copy(T oldDomain) {
        throw new UnsupportedOperationException("Copy domain method not implemented");
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
    protected void updateModes() {
    }

//    private boolean isCopyEnabled() {
//        return this.copyEnabled;
//    }
//
//    public void setCopyEnabled(Boolean copyEnabled) {
//        this.copyEnabled = copyEnabled;
//    }
    private void clearForms() {
        for (EditableForm<T> form : editForms) {
            form.clear();
        }
    }

    /**
     * @return the config
     */
    public DomainEditWindowConfig getConfig() {
        return config;
    }
}
