package pst.arm.client.common.ui.form;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import java.util.ArrayList;
import java.util.List;
import pst.arm.client.common.lang.CommonConstants;
import pst.arm.client.common.ui.grid.BaseGridSimple;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public abstract class PanelFiltrSimple extends FormPanel {

    final protected CommonConstants commonConstants = GWT.create(CommonConstants.class);
    protected LayoutContainer main,mainVerticval;
    private LayoutContainer bottom;
    protected BaseGridSimple resPanel;
    protected List<Field<?>> fields = new ArrayList<Field<?>>();


    public int getEnoughHeight() {
        mainVerticval.layout(true);   
        int min = Integer.valueOf(commonConstants.filtrpanelMinHeight());
        int top = mainVerticval.getHeight() + 5;// getAbsoluteTop();
        return top > min ? top : min;//Integer.valueOf(datagridConstants.filtrpanelMinHeight());//height;
    }

    public PanelFiltrSimple(BaseGridSimple resPanel) {

        this.resPanel = resPanel;
        initView();
        initComponents();
    }

    protected void initView() {      
        setBorders(true);
        setBodyBorder(false);
        setHeaderVisible(false);
        setLayout(new FitLayout());

        mainVerticval = new LayoutContainer();
        
        mainVerticval.setLayout(new RowLayout(Style.Orientation.VERTICAL));
        mainVerticval.setBorders(false);
        mainVerticval.setAutoHeight(true);
        add(mainVerticval);
     
        main = new LayoutContainer();
        main.setLayout(new TableRowLayout());
        main.setBorders(false);
        main.setScrollMode(Style.Scroll.AUTO);
        
        bottom = new LayoutContainer();
        HBoxLayout layoutBottom = new HBoxLayout();
        layoutBottom.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.STRETCHMAX);
        bottom.setLayout(layoutBottom);

        Button bSearch = new Button(commonConstants.search());
        bSearch.addSelectionListener(new SearchSelectionListener());
        bSearch.setWidth(80);
        bSearch.setHeight(24);
        bottom.add(bSearch, new HBoxLayoutData(new Margins(0, 8, 0, 0)));

        Button bClean = new Button(commonConstants.clean());
        bClean.addSelectionListener(new CleanSelectionListener());
        bClean.setWidth(80);
        bClean.setHeight(24);
        bottom.add(bClean, new HBoxLayoutData(new Margins(0, 8, 0, 0)));

        mainVerticval.add(main, new RowData(1, -1, new Margins(0,0,0,0)));
        mainVerticval.add(bottom, new RowData(1, -1,new Margins(2,0,10,0)));
    }

    protected LayoutContainer createColumn() {
        LayoutContainer column = new LayoutContainer();
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.LEFT);
        layout.setLabelWidth(115);
        column.setLayout(layout);
        
        main.add(column, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.TOP));
        return column;
    }

    protected void clean() {
        FormHelper.clearFields(fields);
        resPanel.getCondition().getSearches().clear();
        resPanel.search();
    }

    public class TextFieldSearch extends TextField<String> {

        @Override
        protected void onKeyPress(FieldEvent fe) {
            super.onKeyPress(fe);
            if (fe.getKeyCode() == KeyCodes.KEY_ENTER) {
                search();
            }
        }
    };

    public class SearchSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            search();
        }
    }

    public class CleanSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            clean();
        }
    }
    
    protected CheckBox createCheckBox(String label) {
        CheckBox cb = new CheckBox();
        cb.setBoxLabel(label);
        cb.setLabelSeparator("");
        return cb;
    }

    protected abstract void initComponents();

    protected abstract void search();

    public abstract void fillPanel();
}
