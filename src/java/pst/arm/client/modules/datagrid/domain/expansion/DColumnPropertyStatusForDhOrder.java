/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.domain.expansion;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;
import pst.arm.client.modules.datagrid.domain.*;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class DColumnPropertyStatusForDhOrder extends DColumnPropertyTextField{
     
    @Override
    public GridCellRenderer<BeanModel> createRenderer(final DTable table, final String id) {
        GridCellRenderer<BeanModel> statusRenderer = new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
                DDataGrid row = model.getBean();
                SKeyForColumn key = new SKeyForColumn(property);

                if (!row.getRows().isEmpty() && row.getRows().containsKey(key)) {

                    SKeyForColumn keyDateEnd = new SKeyForColumn("MAIN", "DATEEND");
                    SKeyForColumn keyIsVirt = new SKeyForColumn("MAIN", "IS_VIRT");
                    SKeyForColumn keyIsBill = new SKeyForColumn("MAIN", "IS_BILL");
                    SKeyForColumn keyTypeExec = new SKeyForColumn("MAIN", "TYPE_EXEC");
                    Date curr = new Date();
                    curr = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(curr));
                    String color = "";
                    String qtip = "";
                    QuickTip tip = new QuickTip(grid);
                                        
                    String strDateEnd = (String)null;
                    Date dateEnd = (Date)null;
                    
                    Integer isVirt = 0; 
                    Integer isBill = 0;  
                    Integer typeExec = 0;  

                    if (row.getRows().get(keyIsVirt).getVal() != null) {
                        isVirt = (Integer) row.getRows().get(keyIsVirt).getVal();
                    }
                    if (row.getRows().get(keyIsBill).getVal() != null) {
                        isBill = (Integer) row.getRows().get(keyIsBill).getVal();
                    }
                    if (row.getRows().get(keyTypeExec).getVal() != null) {
                        typeExec = (Integer) row.getRows().get(keyTypeExec).getVal();
                    }
                    if (row.getRows().get(keyDateEnd).getVal() != null) {
                        strDateEnd = (String) row.getRows().get(keyDateEnd).getVal();
                    }
                    if (strDateEnd != null && !strDateEnd.isEmpty()) {
                        dateEnd = DateTimeFormat.getFormat("dd.MM.yyyy").parse(strDateEnd);
                    }
                    
                    if (isVirt != 1 && isBill == 0 && typeExec != 4) {
                       if (dateEnd != null) {
                            if (dateEnd.before(curr) || dateEnd.equals(curr)) {
                                color = "#FF0000"; //красный
                                qtip = "«Реальный» заказ, не завершен, срок прошел";
                            } else if (dateEnd.after(curr)) {
                                color = "#000000"; //черный
                                qtip = "«Реальный» заказ, не выполнен, срок завершения не прошел";

                            }
                        }
                    } else if (isVirt != 1 && isBill == 0 && typeExec == 4) {
                        color = "#00FF00"; //зеленый
                        qtip = "«Реальный» заказ, завершен";
                    } else if (isVirt == 1 || isBill == 1) {
                        color = "#ffff33"; //желтый
                        qtip = "Виртуальный или Счет";
                    }                                      
                    String html = "";
                    if (!color.isEmpty()) {
                        html = "<span " + "qtip='" + qtip + "'" + ">" + "<div style='color: " + color + "; text-align: center; font-size: 14pt'> &#9679; </div>" + "</span>";
                        row.getRows().get(new SKeyForColumn("MAIN:STATUS")).setVal(qtip);
                    }
                    return html;
                }
                return "";
            }
        };
        return statusRenderer;
    }

    @Override
    public ColumnConfig createColumnConfig() //построитель для полей отображения в таблице
    {
        ColumnConfig config = new ColumnConfig("", "", 16);
        config.setWidth(getWidthColumn());
        //config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        //config.setFixed(true);
        return config;
    }
    
    @Override
    public String valueToString( DDataGrid domain, String value) {
        String str = null;
        SKeyForColumn keyDateEnd = new SKeyForColumn("MAIN", "DATEEND");
        SKeyForColumn keyIsVirt = new SKeyForColumn("MAIN", "IS_VIRT");
        SKeyForColumn keyIsBill = new SKeyForColumn("MAIN", "IS_BILL");
        SKeyForColumn keyTypeExec = new SKeyForColumn("MAIN", "TYPE_EXEC");

        if (!domain.getRows().isEmpty()
                && domain.getRows().containsKey(keyIsVirt)
                && domain.getRows().containsKey(keyIsBill)
                && domain.getRows().containsKey(keyTypeExec)) {

            Date curr = new Date();
            curr = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(curr));

            String strDateEnd = (String) null;
            Date dateEnd = (Date) null;

            Integer isVirt = 0;
            Integer isBill = 0;
            Integer typeExec = 0;

            if (domain.getRows().get(keyIsVirt).getVal() != null) {
                isVirt = (Integer) domain.getRows().get(keyIsVirt).getVal();
            }
            if (domain.getRows().get(keyIsBill).getVal() != null) {
                isBill = (Integer) domain.getRows().get(keyIsBill).getVal();
            }
            if (domain.getRows().get(keyTypeExec).getVal() != null) {
                typeExec = (Integer) domain.getRows().get(keyTypeExec).getVal();
            }
            if (domain.getRows().get(keyDateEnd).getVal() != null) {
                strDateEnd = (String) domain.getRows().get(keyDateEnd).getVal();
            }
            if (strDateEnd != null && !strDateEnd.isEmpty()) {
                dateEnd = DateTimeFormat.getFormat("dd.MM.yyyy").parse(strDateEnd);
            }

            if (isVirt != 1 && isBill == 0 && typeExec != 4) {
                if (dateEnd != null) {
                    if (dateEnd.before(curr) || dateEnd.equals(curr)) {
                        str = "«Реальный» заказ, не завершен, срок прошел";
                    } else if (dateEnd.after(curr)) {
                        str = "«Реальный» заказ, не выполнен, срок завершения не прошел";

                    }
                }
            } else if (isVirt != 1 && isBill == 0 && typeExec == 4) {
                str = "«Реальный» заказ, завершен";
            } else if (isVirt == 1 || isBill == 1) {
                str = "Виртуальный или Счет";
            }
        }
        return str;
    }
}
