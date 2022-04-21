package pst.arm.client.modules.test.widgets;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.KeyCodes;


/**
 *
 * @author Admin
 */
public class TestFiltrPanel extends FormPanel {
    public TestFiltrPanel()
    {
        setLayout(new RowLayout(Orientation.HORIZONTAL));

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(50);
        LayoutContainer left = new LayoutContainer(layout);
        LayoutContainer right = new LayoutContainer();

        TextField<String> fio = addTextField(left, "fio", null);
        TextField<String> login = addTextField(left, "login", null);

        CheckBox notActive = new CheckBox();
        notActive.setBoxLabel("box label");
        notActive.setLabelSeparator("");

        left.add(notActive);

        Button bSearch = new Button("search");
        //bSearch.addSelectionListener(new SearchSelectionListener());
        bSearch.setWidth(80);
        bSearch.setHeight(24);

        Button bClean = new Button("clean");
        //bClean.addSelectionListener(new CleanSelectionListener());
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
     
     class TextFieldSearch extends TextField<String> {

        @Override
        protected void onKeyPress(FieldEvent fe) {
            super.onKeyPress(fe);
            if (fe.getKeyCode() == KeyCodes.KEY_ENTER) {
                //search();
            }
        }
     };
}
