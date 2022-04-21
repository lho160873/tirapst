/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionService;
import pst.arm.client.modules.datagrid.service.remote.expansion.GWTDDataGridExpansionServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

/**
 *
 * @author Igor Мб можно будет потом отрефакторить. Пока так, чтобы было всё
 * понятно.
 */
public class DataGridPanelDepartExecutorFactIg extends DataGridPanel {

    private GWTDDataGridExpansionServiceAsync service = GWT.create(GWTDDataGridExpansionService.class);

    public DataGridPanelDepartExecutorFactIg(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelDepartExecutorFactIg(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelDepartExecutorFactIg(String tableName) {
        super(tableName);
    }

    public DataGridPanelDepartExecutorFactIg(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelDepartExecutorFactIg(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    @Override
    protected void onAdd() {
//07.10.2016 LKHorosheva - по просьбе РАВ убрана проверка прав        
                    // метод onAdd()
                    DDataGrid domain = createDomain();//new DDataGrid();
                    //domain.setName(table.getQueryName());

                    if (condition.getFilters().size() > 0) {
                        //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
                        //domain.setName(table.getQueryName());
                        for (Map.Entry filtr : condition.getFilters().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                            IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                            domain.getRows().put(key, val);
                        }
                    }

                    if (isOpenAsSub && passedFields != null) {
                        for (Map.Entry passField : passedFields.entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) passField.getKey(); //ключ по переданной таблице
                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                            IRowColumnVal val = (IRowColumnVal) passField.getValue();
                            domain.getRows().put(key, val);
                        }
                    }

                    if (condition.getValDefault().size() > 0) {
                        //если есть значения по умолчанию задаем их
                        //domain.setName(table.getQueryName());
                        for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) defaultVal.getKey(); //ключ по переданной таблице
                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                            IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                            domain.getRows().put(key, val);
                        }
                    }

                    DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
                    window.addDomainSaveSuccesedListener(saveDataGridListener);
                    window.show();                
/*        String csI = passedFields.get(new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID")).getStringFromVal(
                new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID"), table.getColumnBuilder(new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID")));

        final Integer csId = Integer.parseInt(csI);

        service.checkAccessDepartExecutorFact(csId, ConfigurationManager.getUserId().intValue(), new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    // метод onAdd()
                    DDataGrid domain = createDomain();//new DDataGrid();
                    //domain.setName(table.getQueryName());

                    if (condition.getFilters().size() > 0) {
                        //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
                        //domain.setName(table.getQueryName());
                        for (Map.Entry filtr : condition.getFilters().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                            IRowColumnVal val = (IRowColumnVal) filtr.getValue();
                            domain.getRows().put(key, val);
                        }
                    }

                    if (isOpenAsSub && passedFields != null) {
                        for (Map.Entry passField : passedFields.entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) passField.getKey(); //ключ по переданной таблице
                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                            IRowColumnVal val = (IRowColumnVal) passField.getValue();
                            domain.getRows().put(key, val);
                        }
                    }

                    if (condition.getValDefault().size() > 0) {
                        //если есть значения по умолчанию задаем их
                        //domain.setName(table.getQueryName());
                        for (Map.Entry defaultVal : condition.getValDefault().entrySet()) {
                            SKeyForColumn key = (SKeyForColumn) defaultVal.getKey(); //ключ по переданной таблице
                            //SKeyForColumn val = (SKeyForColumn) key.getValue();
                            IRowColumnVal val = (IRowColumnVal) defaultVal.getValue();
                            domain.getRows().put(key, val);
                        }
                    }

                    DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
                    window.addDomainSaveSuccesedListener(saveDataGridListener);
                    window.show();
                } else {
                    service.getUserDepartExecutorFact(csId, new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public void onSuccess(String result) {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к " + result, null);
                        }
                    });

                }
            }
        });

*/

    }

    @Override
    protected void onEdit() {
//07.10.2016 LKHorosheva - по просьбе РАВ убрана проверка прав 
        // метод onEdit()
        DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();


        DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
        window.addDomainSaveSuccesedListener(saveDataGridListener);
        window.show();

        /*        String csI = passedFields.get(new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID")).getStringFromVal(
                new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID"), table.getColumnBuilder(new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID")));

        final Integer csId = Integer.parseInt(csI);

        service.checkAccessDepartExecutorFact(csId, ConfigurationManager.getUserId().intValue(), new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    // метод onEdit()
                    DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();


                    DataGridEditWindow window = new DataGridEditWindow(domain, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
                    window.addDomainSaveSuccesedListener(saveDataGridListener);
                    window.show();
                } else {
                    service.getUserDepartExecutorFact(csId, new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public void onSuccess(String result) {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к " + result, null);
                        }
                    });

                }
            }
        });
        */

    }

    @Override
    protected void onDelete() {
//07.10.2016 LKHorosheva - по просьбе РАВ убрана проверка прав 
                            // метод onDelete()
                    MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                                final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                                List<DDataGrid> domains = new ArrayList<DDataGrid>();
                                for (final BeanModel beanModel : selection) {
                                    DDataGrid domain = beanModel.getBean();
                                    domains.add(domain);
                                }
                                AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        //MessageBox.alert(commonConstants.error(), datagridMessages.errorDelete(), null).show();
                                        AppHelper.showMsgRpcServiceError(caught);
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        //reloadGrid();
                                        for (final BeanModel beanModel : selection) {
                                            grid.getStore().remove(beanModel);
                                        }
//                            loader2.load();
                                        selectCurrentRow();
                                    }
                                };
                                getService().deleteDomains(tableName, domains, callback);
                            }
                        }});

/*        String csI = passedFields.get(new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID")).getStringFromVal(
                new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID"), table.getColumnBuilder(new SKeyForColumn("WORK_PLAN:CONTRACT_STAGE_ID")));

        final Integer csId = Integer.parseInt(csI);

        service.checkAccessDepartExecutorFact(csId, ConfigurationManager.getUserId().intValue(), new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    // метод onDelete()
                    MessageBox.confirm(commonConstants.confirm(), datagridMessages.confirmDelete(), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked() == be.getDialog().getButtonById(Dialog.YES)) {
                                final List<BeanModel> selection = grid.getSelectionModel().getSelection();
                                List<DDataGrid> domains = new ArrayList<DDataGrid>();
                                for (final BeanModel beanModel : selection) {
                                    DDataGrid domain = beanModel.getBean();
                                    domains.add(domain);
                                }
                                AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        //MessageBox.alert(commonConstants.error(), datagridMessages.errorDelete(), null).show();
                                        AppHelper.showMsgRpcServiceError(caught);
                                    }

                                    @Override
                                    public void onSuccess(Boolean result) {
                                        //reloadGrid();
                                        for (final BeanModel beanModel : selection) {
                                            grid.getStore().remove(beanModel);
                                        }
//                            loader2.load();
                                        selectCurrentRow();
                                    }
                                };
                                getService().deleteDomains(tableName, domains, callback);
                            }
                        }
                    });
                } else {
                    service.getUserDepartExecutorFact(csId, new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public void onSuccess(String result) {
                            MessageBox.alert("Внимание!", "У Вас нет прав на корректировку данного ОКП. Обратитесь к " + result, null);
                        }
                    });

                }
            }
        });
*/
    }
}
