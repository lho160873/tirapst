package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import pst.arm.client.common.AppHelper;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DRowColumnValDate;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.service.remote.GWTDDataGridServiceAsync;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;

/**
 *
 * @author LKHorosheva, Igor
 */
public class DataGridEditWindowOfficeDocContrIg extends DataGridEditWindow {

    protected TabPanel tabPanelSub; //панель с закладками для отображения подчиненных данных (дополнительная информация)

    public DataGridEditWindowOfficeDocContrIg(DDataGrid domain, DTable table, EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
        addBtnInToolBar();
        initPanelSub();
    }

    public DataGridEditWindowOfficeDocContrIg(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
        addBtnInToolBar();
        initPanelSub();
    }

    public DataGridEditWindowOfficeDocContrIg(DDataGrid domain, DTable table, EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
        addBtnInToolBar();
        initPanelSub();
    }

    private void addBtnInToolBar() {
        Button btnMark = new Button(datagridConstants.btnMark());
        //btnMark.setIcon(AbstractImagePrototype.create(commonImages.edit()));
        btnMark.setIconAlign(Style.IconAlign.LEFT);
        getToolBar().remove(btnCancel);
        getToolBar().add(btnMark);
        getToolBar().add(btnCancel);

        btnMark.addSelectionListener(new SelectionListener() {

            @Override
            public void componentSelected(ComponentEvent ce) {
                mark();
            }
        });
    }

    protected final void initPanelSub() {
        tabPanelSub = new TabPanel();
        tabPanelSub.setBorders(false);
        tabPanelSub.setBodyBorder(false);
        BorderLayoutData panelDataSub = new BorderLayoutData(LayoutRegion.SOUTH);//, 300);
        panelDataSub.setMargins(new Margins(1, 0, 0, 0));
        panelDataSub.setSplit(true);
        main.add(tabPanelSub, panelDataSub);
        for (SRelationInfo subTable : table.getSubTables()) {
            final String tabName = subTable.getQueryName();
            final String caption = subTable.getCaption();
            DataGridPanel panelSub = (DataGridPanel) DataGridPanelBuilder.createDataGridPanel(tabName, subTable);
            TabItem item = new TabItem();
            item.setClosable(false);
            item.setBorders(false);
            item.setItemId(tabName);
            item.setText(caption);
            item.setLayout(new FitLayout());
            item.add(panelSub);
            tabPanelSub.add(item);
            panelSub.putGridSubFilters(domain);
        }
    }

    protected void mark() {
        DDataGrid oldDomain = new DDataGrid();
        oldDomain.copy(domain);

        String format = "dd.MM.yyyy";
        Date date = DateTimeFormat.getFormat(format).parse(DateTimeFormat.getFormat(format).format(new Date()));

        SKeyForColumn key = new SKeyForColumn("MAIN:DATE_FACT");
        IRowColumnVal val = new DRowColumnValDate();
        if (domain.getRows().get(key).getVal() == null) {
            val.setVal(date);
            domain.getRows().put(key, val);
            AsyncCallback<DDataGrid> callback = new AsyncCallback<DDataGrid>() {

                @Override
                public void onFailure(Throwable caught) {
                    AppHelper.showMsgRpcServiceError(caught);
                }

                @Override
                public void onSuccess(DDataGrid t) {
                    if (t == null) {
                        onFailure(null);
                    } else {
                        getDomain().copy(t);
                        updateFormsFromDomain();
                        notifyDomainSaveSuccesed();
                        getEditPanel().unmask();
                        setHeading(getHeaderViewEdit());
                        if (state.equals(EditState.NEW)) {
                            state = EditState.EDIT;
                            updateControlsVisibility();
                        }

                    }
                }
            };
            ((GWTDDataGridServiceAsync) getService()).save(tableName, domain, oldDomain, false, callback);
        }
    }
}
