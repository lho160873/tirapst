package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import pst.arm.client.common.AppHelper;

/**
 *
 * @author wesStyle
 */
public class DataGridPanelWD extends DataGridPanel {

    public DataGridPanelWD(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelWD(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelWD(String tableName) {
        super(tableName);
    }

    public DataGridPanelWD(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelWD(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

    protected void onAdd() {
        final AsyncCallback<List<DDataGrid>> callback_getNorm = new AsyncCallback<List<DDataGrid>>() {
            public void onSuccess(List<DDataGrid> result) {
                DDataGrid domain = new DDataGrid();

                SKeyForColumn nk = new SKeyForColumn("MAIN:AVERAGE_SALARY");
                final Double valnk = (Double) result.get(0).getRows().get(nk).getVal();
                final IRowColumnVal ival = new IRowColumnVal();
                ival.setVal(valnk);
                DDataGrid ns = new DDataGrid();
                ns.getRows().put(nk, ival);

                if (condition.getFilters().size() > 0) {
                    //если таблица открыта в режиме отображения подчиненных данных передаем в форму добавления новой записи значение для полей по которой на текущий момент произведена фильтрация данных
                    domain.setName(table.getQueryName());
                    for (Map.Entry filtr : condition.getFilters().entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) filtr.getKey(); //ключ по переданной таблице
                        //SKeyForColumn val = (SKeyForColumn) key.getValue();
                        IRowColumnVal val = (IRowColumnVal) filtr.getValue();
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

                if (isOpenAsSub && passedFields != null) {
                    for (Map.Entry passField : passedFields.entrySet()) {
                        SKeyForColumn key = (SKeyForColumn) passField.getKey(); //ключ по переданной таблице
                        //SKeyForColumn val = (SKeyForColumn) key.getValue();
                        IRowColumnVal val = (IRowColumnVal) passField.getValue();
                        domain.getRows().put(key, val);
                    }
                }

                Double costs = 0.;
                Double labours = 0.;
                SKeyForColumn costsKey = new SKeyForColumn("MAIN:PLAN_COST");
                SKeyForColumn labourKey = new SKeyForColumn("MAIN:PLAN_LABOUR");

                for (Object obj: store.getModels()) {
                    BeanModel model = (BeanModel) obj;
                    DDataGrid storeRow = model.getBean();

                    costs += doubleParser(storeRow.getRows().get(costsKey).getStringFromVal(
                            costsKey, table.getColumnBuilder(costsKey)
                    ));
                    labours += doubleParser(storeRow.getRows().get(labourKey).getStringFromVal(
                            labourKey, table.getColumnBuilder(labourKey)
                    ));
                }

                Double planLabourVal;
                SKeyForColumn planLabourKey = new SKeyForColumn("JUI:PLAN_LABOUR");
                if (passedFields.get(planLabourKey).getVal() == null)
                    planLabourVal = 0.;
                else
                    planLabourVal = doubleParser(
                        passedFields.get(planLabourKey).getStringFromVal(
                                labourKey, table.getColumnBuilder(labourKey)
                        )
                );

                Double planCostVal;
                SKeyForColumn planCostKey = new SKeyForColumn("JUI:PLAN_COST");
                if (passedFields.get(planCostKey).getVal() == null)
                    planCostVal = 0.;
                else
                    planCostVal = doubleParser(
                        passedFields.get(planCostKey).getStringFromVal(
                                costsKey, table.getColumnBuilder(costsKey)
                        )
                    );

                IRowColumnVal costsIVal = new DRowColumnValNumber();
                costsIVal.setVal(planCostVal - costs);
                IRowColumnVal labourIVal = new DRowColumnValNumber();
                labourIVal.setVal(planLabourVal - labours);

                domain.getRows().put(labourKey, labourIVal);
                DColumnPropertyNumberField numbPropLabour = (DColumnPropertyNumberField)table.getColumnBuilder(labourKey).getColumn(
                        labourKey
                ).getColumnProperty();
                numbPropLabour.setMinValue(0);
                numbPropLabour.setMaxValue(planLabourVal - labours);

                domain.getRows().put(costsKey, costsIVal);
                DColumnPropertyNumberField numbPropCost = (DColumnPropertyNumberField) table.getColumnBuilder(costsKey).getColumn(
                        costsKey
                ).getColumnProperty();
                numbPropCost.setMinValue(0);
                numbPropCost.setMaxValue(planCostVal - costs);


                SKeyForColumn begKey = new SKeyForColumn("MAIN:BEG_DATE");
                SKeyForColumn endKey = new SKeyForColumn("MAIN:END_DATE");

                DColumnPropertyDateField datePropBegin = (DColumnPropertyDateField) table.getColumnBuilder(begKey).getColumn(
                        begKey
                ).getColumnProperty();
                DColumnPropertyDateField datePropEnd = (DColumnPropertyDateField) table.getColumnBuilder(endKey).getColumn(
                        endKey
                ).getColumnProperty();
                Date planBegDate = (Date)domain.getRows().get(begKey).getFormatVal(begKey, table.getColumnBuilder(begKey));
                Date planBegEnd = (Date) domain.getRows().get(endKey).getFormatVal(endKey, table.getColumnBuilder(endKey));
                datePropBegin.setMinDate(planBegDate);
                datePropBegin.setMaxDate(planBegEnd);
                datePropEnd.setMinDate(planBegDate);
                datePropEnd.setMaxDate(planBegEnd);

                DataGridEditWindow window = new DataGridEditWindowWorkPlan(domain, ns, table, Editable.EditState.NEW, EWindowType.EWT_WINDOW);
                window.addDomainSaveSuccesedListener(saveDataGridListener);
                window.show();
            }

            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

        };
        getService().getDataGrid("NORMATIVE_VALUES", new DataGridSearchCondition(), callback_getNorm);
    }

    private Double doubleParser(String numb) {
        return Double.parseDouble(numb.replace(",", "."));
    }

    protected void onEdit() {
        final AsyncCallback<List<DDataGrid>> callback_getNorm = new AsyncCallback<List<DDataGrid>>() {
            public void onSuccess(List<DDataGrid> result) {
                DDataGrid domain = ((BeanModel) grid.getSelectionModel().getSelectedItem()).getBean();

                SKeyForColumn nk = new SKeyForColumn("MAIN:AVERAGE_SALARY");
                final Double valnk = (Double) result.get(0).getRows().get(nk).getVal();
                final IRowColumnVal ival = new IRowColumnVal();
                ival.setVal(valnk);
                DDataGrid ns = new DDataGrid();
                ns.getRows().put(nk, ival);

                Double costs = 0.;
                Double labours = 0.;
                SKeyForColumn costsKey = new SKeyForColumn("MAIN:PLAN_COST");
                SKeyForColumn labourKey = new SKeyForColumn("MAIN:PLAN_LABOUR");

                SKeyForColumn wpId = new SKeyForColumn("MAIN:DEP_EX_PLAN_ID");
                String idB = domain.getRows().get(wpId).getStringFromVal(
                        wpId, table.getColumnBuilder(wpId));

                for (Object obj : store.getModels()) {
                    BeanModel model = (BeanModel) obj;
                    DDataGrid storeRow = model.getBean();
                    String idS = storeRow.getRows().get(wpId).getStringFromVal(
                            wpId, table.getColumnBuilder(wpId));

                    if (idB.equals(idS))
                        continue;

                    costs += doubleParser(storeRow.getRows().get(costsKey).getStringFromVal(
                            costsKey, table.getColumnBuilder(costsKey)
                    ));
                    labours += doubleParser(storeRow.getRows().get(labourKey).getStringFromVal(
                            labourKey, table.getColumnBuilder(labourKey)
                    ));
                }

                Double planLabourVal;
                SKeyForColumn planLabourKey = new SKeyForColumn("JUI:PLAN_LABOUR");
                if (passedFields.get(planLabourKey).getVal() == null)
                    planLabourVal = 0.;
                else
                    planLabourVal = doubleParser(
                            passedFields.get(planLabourKey).getStringFromVal(
                                    labourKey, table.getColumnBuilder(labourKey)
                            )
                    );

                Double planCostVal;
                SKeyForColumn planCostKey = new SKeyForColumn("JUI:PLAN_COST");
                if (passedFields.get(planCostKey).getVal() == null)
                    planCostVal = 0.;
                else
                    planCostVal = doubleParser(
                            passedFields.get(planCostKey).getStringFromVal(
                                    costsKey, table.getColumnBuilder(costsKey)
                            )
                    );

                IRowColumnVal costsIVal = new DRowColumnValNumber();
                costsIVal.setVal(planCostVal - costs);
                IRowColumnVal labourIVal = new DRowColumnValNumber();
                labourIVal.setVal(planLabourVal - labours);

                domain.getRows().put(labourKey, labourIVal);
                DColumnPropertyNumberField numbPropLabour = (DColumnPropertyNumberField) table.getColumnBuilder(labourKey).getColumn(
                        labourKey
                ).getColumnProperty();
                numbPropLabour.setMinValue(0);
                numbPropLabour.setMaxValue(planLabourVal - labours);

                domain.getRows().put(costsKey, costsIVal);
                DColumnPropertyNumberField numbPropCost = (DColumnPropertyNumberField) table.getColumnBuilder(costsKey).getColumn(
                        costsKey
                ).getColumnProperty();
                numbPropCost.setMinValue(0);
                numbPropCost.setMaxValue(planCostVal - costs);


                SKeyForColumn begKey = new SKeyForColumn("MAIN:BEG_DATE");
                SKeyForColumn endKey = new SKeyForColumn("MAIN:END_DATE");

                DColumnPropertyDateField datePropBegin = (DColumnPropertyDateField) table.getColumnBuilder(begKey).getColumn(
                        begKey
                ).getColumnProperty();
                DColumnPropertyDateField datePropEnd = (DColumnPropertyDateField) table.getColumnBuilder(endKey).getColumn(
                        endKey
                ).getColumnProperty();
                Date planBegDate = (Date) domain.getRows().get(begKey).getFormatVal(begKey, table.getColumnBuilder(begKey));
                Date planBegEnd = (Date) domain.getRows().get(endKey).getFormatVal(endKey, table.getColumnBuilder(endKey));
                datePropBegin.setMinDate(planBegDate);
                datePropBegin.setMaxDate(planBegEnd);
                datePropEnd.setMinDate(planBegDate);
                datePropEnd.setMaxDate(planBegEnd);

                DataGridEditWindow window = new DataGridEditWindowWorkPlan(domain, ns, table, Editable.EditState.EDIT, EWindowType.EWT_WINDOW);
                window.addDomainSaveSuccesedListener(saveDataGridListener);
                window.show();
            }

            public void onFailure(Throwable caught) {
                AppHelper.showMsgRpcServiceError(caught);
            }

        };
        getService().getDataGrid("NORMATIVE_VALUES", new DataGridSearchCondition(), callback_getNorm);
    }
}
