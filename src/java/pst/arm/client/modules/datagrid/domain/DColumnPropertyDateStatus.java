/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.domain;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyDateStatus extends DColumnPropertyTextField{
     
    @Override
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> statusRenderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                //SKeyForColumn keyFact = new SKeyForColumn(property);

                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {
                    IColumnBuilder builder = table.getColumnBuilder(key);              
                    
                    SKeyForColumn keyFact = (((DColumnBuilderDateStatus) builder).getSecondDate());
                    SKeyForColumn keyPlan = (((DColumnBuilderDateStatus) builder).getMainDate());
                    
                    Date fact = (Date) row.getRows().get(keyFact).getVal();
                    if (fact != null) {
                        fact = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(fact));
                    }

                    Date plan = (Date) row.getRows().get(keyPlan).getVal();
                    if (plan != null) {
                        plan = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(plan));
                    }

                    Date curr = new Date();
                    if (curr != null) {
                        curr = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(curr));
                    }

                    String color = "";
                    String qtip = "";
                    QuickTip tip = new QuickTip(grid);
                    
                    if (((plan.after(curr)) || (plan.equals(curr))) && (fact == null)) {
                        color = "#000000";
                        qtip = "Назначено. Срок выполнения не прошел";
                        row.getRows().get(new SKeyForColumn("MAIN:DATE_FACT_STAT")).setVal("Назначено. Срок выполнения не прошел");
                    } else if ((plan.before(curr)) && (fact == null)) {
                        color = "#FF0000";
                        qtip = "Не выполнено. Срок исполнения прошел";
                        row.getRows().get(new SKeyForColumn("MAIN:DATE_FACT_STAT")).setVal("Не выполнено. Срок исполнения прошел");
                    } else if (((plan.after(fact)) || (plan.equals(fact)))) {
                        color = "#00FF00";
                        qtip = "Выполнено в срок";
                        row.getRows().get(new SKeyForColumn("MAIN:DATE_FACT_STAT")).setVal("Выполнено в срок");
                    } else if (plan.before(fact)) {
                        color = "#0000FF";
                        qtip = "Выполнено  с нарушением срока";
                        row.getRows().get(new SKeyForColumn("MAIN:DATE_FACT_STAT")).setVal("Выполнено  с нарушением срока");
                    }

                    String html = "<span " + "qtip='" + qtip + "'" + ">" + "<div style='color: " + color + "; text-align: center; font-size: 14pt'> &#9679; </div>" + "</span>";
                    return html;
                }

                return "";
            }
        };
        return statusRenderer;
    }

    @Override
    public ColumnConfig createColumnConfig( ) //построитель для полей отображения в таблице
    {
        ColumnConfig config = new ColumnConfig("", "", 16);
        config.setWidth(getWidthColumn());
        //config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        //config.setFixed(true);
        return config;
    }


}
