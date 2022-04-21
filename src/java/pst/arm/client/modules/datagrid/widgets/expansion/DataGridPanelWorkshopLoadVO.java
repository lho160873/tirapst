package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ConfigurationManager;
import pst.arm.client.common.service.FileReportServiceCallback;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.controls.editdomain.listener.DomainSaveSuccesedListener;
import pst.arm.client.common.ui.form.Editable.EditMode;
import pst.arm.client.common.ui.form.Editable.EditState;
import pst.arm.client.common.ui.grid.GridTools;
import pst.arm.client.modules.datagrid.domain.*;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.domain.search.DataGridSearchCondition;
import pst.arm.client.modules.datagrid.event.DataGridEvent;
import pst.arm.client.modules.datagrid.event.DataGridEvents;
import pst.arm.client.modules.datagrid.widgets.*;

import java.util.*;

/**
 *
 * @author LKHorosheva, wesStyle
 * @since 0.0.1
 */
public class DataGridPanelWorkshopLoadVO extends DataGridPanel {

    @Override
    public void enabledBtn() {
        //настройка пров доступа
        String datagridName = tableName.toUpperCase();
        isDictAdd = (mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictAdd")
                || ConfigurationManager.getPropertyAsBoolean("dictAdd_" + datagridName));
        isDictEdit = (mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictEdit")
                || ConfigurationManager.getPropertyAsBoolean("dictEdit_" + datagridName));
        isDictDelete = (mode != EditMode.VIEWONLY)
                && (ConfigurationManager.getPropertyAsBoolean("dictEditFull")
                || ConfigurationManager.getPropertyAsBoolean("dictEditFull_" + datagridName)
                || ConfigurationManager.getPropertyAsBoolean("dictDelete")
                || ConfigurationManager.getPropertyAsBoolean("dictDelete_" + datagridName));
        isDictView = (ConfigurationManager.getPropertyAsBoolean("dictView")
                || ConfigurationManager.getPropertyAsBoolean("dictView_" + datagridName));
        isSprDictEdit = (isDictAdd || isDictEdit || isDictDelete || isDictView);

        btnAdd.setVisible(false);
        menuItemAdd.setVisible(false);

        btnEdit.setVisible(false);
        menuItemEdit.setVisible(false);

        btnDelete.setVisible(false);
        menuItemDelete.setVisible(false);

        btnView.setVisible(false);
        menuItemView.setVisible(false);

        sprEdit.setVisible(false);
        sprMenuItemEdit.setVisible(false);

        updateToolBar();
    }

    @Override
    public void search() {
        //loader.load();

        if (!condition.getSearches().isEmpty() && condition.getSearches().get(new SKeyForColumn("MAIN:DATE_PICKER")).getVal() != null) {
            String date = (String) condition.getSearches().get(new SKeyForColumn("MAIN:DATE_PICKER")).getVal();
            picked = DateTimeFormat.getFormat("yyyy-MM-dd").parse(date);
            //condition.getSearches().clear();
        } else picked = new Date();

        reloadGrid();
    }

    Date picked = new Date();

    @Override
    protected void showReport(final String reportType) {
        if (table.getReportTemplate() == null) {
            final Component cmp = this;

            getService().getDataGrid(tableName, condition, new AsyncCallback<List<DDataGrid>>() {
                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                }

                @Override
                public void onSuccess(List<DDataGrid> result) {

                    ArrayList<String> headers = new ArrayList<String>();
                    headers.add(grid.getColumnModel().getColumnHeader(3));
                    headers.add(grid.getColumnModel().getColumnHeader(4));
                    headers.add(grid.getColumnModel().getColumnHeader(5));
                    headers.add(grid.getColumnModel().getColumnHeader(6));
                    headers.add(grid.getColumnModel().getColumnHeader(7));
                    headers.add(grid.getColumnModel().getColumnHeader(8));

                    getReportService().generateHtmlArchiveStoreEntityReportWorkshopLoad(tableName, headers, convertDomainToListOfHashMaps(result, table), reportType, new FileReportServiceCallback(cmp));
                }
            });


        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (table.getReportParams() != null) {
                for (Map.Entry<String, String> tp : table.getReportParams().entrySet()) {
                    for (Map.Entry filtr : condition.getFilters().entrySet()) {
                        SKeyForColumn fKey = (SKeyForColumn) filtr.getKey();
                        IRowColumnVal fVal = (IRowColumnVal) filtr.getValue();
                        String fStr = fVal.getStringFromVal(fKey, table.getColumnBuilder(fKey));
                        if (fKey.equals(new SKeyForColumn(tp.getValue()))) {
                            if (fStr == null) {
                                params.put(tp.getKey(), "");
                            } else {
                                params.put(tp.getKey(), fStr);
                            }
                        }
                    }
                }
            }

            getReportService().generateJasperReport(table.getReportTemplate(), reportType, params, condition, table, new FileReportServiceCallback(this));
        }

    }

    @Override
    public void reloadGrid() {
        super.reloadGrid();
        if (picked != null) {

            Date c = picked;
            Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));
            grid.getColumnModel().setColumnHeader(3, "План на " + monthName(m) + " " + y + ", чел/час.");


            c = (Date) picked.clone();
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            c.setMonth(m);
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));
            grid.getColumnModel().setColumnHeader(4, "План на " + monthName(m) + " " + y + ", чел/час.");

            c = (Date) picked.clone();
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            c.setMonth(m + 1);
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));
            grid.getColumnModel().setColumnHeader(5, "План на " + monthName(m) + " " + y + ", чел/час.");

            c = (Date) picked.clone();
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            c.setMonth(m + 2);
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));
            grid.getColumnModel().setColumnHeader(6, "План на " + monthName(m) + " " + y + ", чел/час.");

            c = (Date) picked.clone();
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            c.setMonth(m + 3);
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));
            grid.getColumnModel().setColumnHeader(7, "План на " + monthName(m) + " " + y + ", чел/час.");
            c = (Date) picked.clone();
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            c.setMonth(m + 4);
            m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
            y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));
            grid.getColumnModel().setColumnHeader(8, "План на " + monthName(m) + " " + y + ", чел/час.");
        }
    }

    @Override
    protected ColumnModel createColumModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        //пустая колонка для индикации
        ColumnConfig config = new ColumnConfig("nullCloumn", "", 16);
        config.setResizable(false);
        config.setSortable(false);
        config.setMenuDisabled(true);
        config.setFixed(true);
        columns.add(config);

        summary = new AggregationRowConfig<BeanModel>();

        for (IColumnBuilder builder : table.getColumnBuilders()) {
            for (Map.Entry colEntry : builder.getColumns().entrySet()) {
                SKeyForColumn key = (SKeyForColumn) colEntry.getKey();
                config = builder.createColumnConfig(key, table, getId());
                setAggregations(config);

                if (key.equals(new SKeyForColumn("MAIN:M1"))) {
                    Date c = new Date();
                    Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

                    config.setHeader("План на " + monthName(m) + " " + y + ", чел/час.");
                }

                if (key.equals(new SKeyForColumn("MAIN:M2"))) {
                    Date c = new Date();
                    Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    c.setMonth(m);
                    m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

                    config.setHeader("План на " + monthName(m) + " " + y + ", чел/час.");
                }

                if (key.equals(new SKeyForColumn("MAIN:M3"))) {
                    Date c = new Date();
                    Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    c.setMonth(m + 1);
                    m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

                    config.setHeader("План на " + monthName(m) + " " + y + ", чел/час.");
                }

                if (key.equals(new SKeyForColumn("MAIN:M4"))) {
                    Date c = new Date();
                    Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    c.setMonth(m + 2);
                    m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

                    config.setHeader("План на " + monthName(m) + " " + y + ", чел/час.");
                }

                if (key.equals(new SKeyForColumn("MAIN:M5"))) {
                    Date c = new Date();
                    Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    c.setMonth(m + 3);
                    m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

                    config.setHeader("План на " + monthName(m) + " " + y + ", чел/час.");
                }

                if (key.equals(new SKeyForColumn("MAIN:M6"))) {
                    Date c = new Date();
                    Integer m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    c.setMonth(m + 4);
                    m = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format( c ));
                    Integer y = Integer.parseInt(DateTimeFormat.getFormat( "yyyy" ).format( c ));

                    config.setHeader("План на " + monthName(m) + " " + y + ", чел/час.");
                }

                columns.add(config);
            }
        }

        ColumnModel cm = new ColumnModel(columns);
        cm.addAggregationRow(summary);
        return cm;
    }

    protected String monthName(int m) {
        switch (m) {
            case 1: return "январь";
            case 2: return "февраль";
            case 3: return "март";
            case 4: return "апрель";
            case 5: return "май";
            case 6: return "июнь";
            case 7: return "июль";
            case 8: return "август";
            case 9: return "сентябрь";
            case 10: return "октябрь";
            case 11: return "ноябрь";
            case 12: return "декабрь";
        }
        return "";
    }

    public DataGridPanelWorkshopLoadVO(DTable table, String tn, DCondition cnd) {
        super(table, tn, cnd);
    }

    public DataGridPanelWorkshopLoadVO(DTable table, String tn) {
        super(table, tn);
    }

    public DataGridPanelWorkshopLoadVO(String tableName) {
        super(tableName);
    }

    public DataGridPanelWorkshopLoadVO(String tableName, SRelationInfo relationInfo) {
        super(tableName, relationInfo);
    }

    public DataGridPanelWorkshopLoadVO(DTable table, String tableName, SRelationInfo relationInfo) {
        super(table, tableName, relationInfo);
    }

}