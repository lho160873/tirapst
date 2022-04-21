package pst.arm.client.modules.datagrid.widgets.expansion;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import pst.arm.client.common.ui.controls.editdomain.EWindowType;
import pst.arm.client.common.ui.form.Editable;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.DTable;
import pst.arm.client.modules.datagrid.domain.SRelationInfo;
import pst.arm.client.modules.datagrid.domain.expansion.DCondition;
import pst.arm.client.modules.datagrid.widgets.DataGridEditWindow;
import pst.arm.client.modules.datagrid.widgets.DataGridPanel;
import pst.arm.client.modules.datagrid.widgets.DataGridPanelBuilder;

/**
 *
 * @author Igor Окно в Контроль производства - Контроль - Кол-во невып. приказов
 * и распоряжений - Невыполненные прикащы и распоряжения - Двойной щелчок по
 * какой-либо строке, аналогия с DataGridEditWindowOfficeDocContrIg.java
 */
public class DataGridEditWindowOfficeDocContrIgControl extends DataGridEditWindow {

    protected TabPanel tabPanelSub; //панель с закладками для отображения подчиненных данных (дополнительная информация)

    public DataGridEditWindowOfficeDocContrIgControl(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType) {
        super(domain, table, state, windowType);
        initPanelSub();
    }

    public DataGridEditWindowOfficeDocContrIgControl(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType, DCondition cnd) {
        super(domain, table, state, windowType, cnd);
        initPanelSub();
    }

    public DataGridEditWindowOfficeDocContrIgControl(DDataGrid domain, DTable table, Editable.EditState state, EWindowType windowType, DCondition cnd, String customHeader) {
        super(domain, table, state, windowType, cnd, customHeader);
        initPanelSub();
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
            //кнопки редактирования "насильно" делаем доступными, т.к. считаем что эту форму
            //будем открывать из сообщения, которое адресовано тому, кто имеет права работы с этими данными
            if (tabName.equals("OFFICE_DOC_EXEC_IG")) {

                panelSub.getTopToolbar().remove(panelSub.sprReport);
                panelSub.getTopToolbar().remove(panelSub.btnReportSplit);
                panelSub.getTopToolbar().remove(panelSub.ftiFiltr);

                panelSub.removeOrAddBtnOnToolBar(true, panelSub.sprEdit);
                panelSub.removeOrAddBtnOnToolBar(true, panelSub.btnAdd);
                panelSub.removeOrAddBtnOnToolBar(true, panelSub.btnEdit);
                panelSub.removeOrAddBtnOnToolBar(true, panelSub.btnDelete);
                panelSub.removeOrAddBtnOnToolBar(true, panelSub.btnView);

                panelSub.removeOrAddBtnOnToolBar(true, panelSub.sprReport);
                panelSub.removeOrAddBtnOnToolBar(true, panelSub.btnReportSplit);

                panelSub.sprEdit.setVisible(true);
                panelSub.btnAdd.setVisible(true);
                panelSub.btnDelete.setVisible(true);
                panelSub.btnEdit.setVisible(true);
                panelSub.btnView.setVisible(true);
            }
        }
    }

    @Override
    protected void initHeading() {
        if (wnd != null) {
            wnd.setHeading("Приказы и распоряжения – исполнение");
        } else {
            this.setHeading("Приказы и распоряжения – исполнение");

        }
    }
}
