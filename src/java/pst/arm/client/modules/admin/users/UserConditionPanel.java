package pst.arm.client.modules.admin.users;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import pst.arm.client.modules.admin.domain.UserSearchCondition;
import pst.arm.client.modules.admin.lang.AdminConstants;

/**
 *
 * @author maslov
 */
public class UserConditionPanel extends FormPanel {

    protected AdminConstants constants;
    private TextField<String> fio;
    private TextField<String> login;
    private CheckBox notActive;
    private UsersResultPanel resPanel;

    public UserConditionPanel(UsersResultPanel resPanel) {
        this.resPanel = resPanel;
        constants = (AdminConstants) GWT.create(AdminConstants.class);
        setHeading(constants.titleConditionPanel());
        initView();
    }

    private void initView() {
        setLayout(new RowLayout(Orientation.HORIZONTAL));

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(50);
        LayoutContainer left = new LayoutContainer(layout);
        LayoutContainer right = new LayoutContainer();

        fio = addTextField(left, constants.fioField(), null);
        login = addTextField(left, constants.loginField(), null);

        notActive = new CheckBox();
        notActive.setBoxLabel(constants.showDisabledField());
        notActive.setLabelSeparator("");

        left.add(notActive);

        Button bSearch = new Button(constants.search());
        bSearch.addSelectionListener(new SearchSelectionListener());
        bSearch.setWidth(80);
        bSearch.setHeight(24);

        Button bClean = new Button(constants.clean());
        bClean.addSelectionListener(new CleanSelectionListener());
        bClean.setWidth(80);
        bClean.setHeight(24);

        FormData fl = new FormData();
        fl.setMargins(new Margins(0, 0, 4, 0));
        right.add(bSearch, fl);
        right.add(bClean, fl);

        add(left, new RowData(250, 1.0, new Margins(2, 10, 0, 10)));
        add(right, new RowData(100, 1.0, new Margins(0, 10, 0, 10)));
    }

    private TextField<String> addTextField(LayoutContainer panel, String label, String emptyText) {
        TextField<String> field = new TextFieldSearch();
        field.setFieldLabel(label);
        field.setEmptyText(emptyText);
        panel.add(field, new FormData("100%"));
        return field;
    }

    private void search() {
        UserSearchCondition condition = new UserSearchCondition();
        condition.setFio(fio.getValue());
        condition.setLogin(login.getValue());
        condition.setSearchNotActive(notActive.getValue());

        resPanel.search(condition);
    }

    private void clean() {
        fio.setValue(null);
        login.setValue(null);
        notActive.setValue(false);
        search();
    }

    class TextFieldSearch extends TextField<String> {

        @Override
        protected void onKeyPress(FieldEvent fe) {
            super.onKeyPress(fe);
            if (fe.getKeyCode() == KeyCodes.KEY_ENTER) {
                search();
            }
        }
    };

    private class SearchSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            search();
        }
    }

    private class CleanSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            clean();
        }
    }
}
