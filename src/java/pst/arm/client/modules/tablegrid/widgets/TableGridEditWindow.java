package pst.arm.client.modules.tablegrid.widgets;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import pst.arm.client.common.service.remote.GWTEditServiceAsync;
import pst.arm.client.common.ui.controls.editdomain.DomainEditWindow;
import pst.arm.client.modules.tablegrid.domain.TableGrid;
import pst.arm.client.modules.tablegrid.service.remote.GWTTableGridService;
import pst.arm.client.modules.tablegrid.service.remote.GWTTableGridServiceAsync;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGridEditWindow extends DomainEditWindow<TableGrid>{
    
    private GWTTableGridServiceAsync service = GWT.create(GWTTableGridService.class);
    private TableGridEditableForm pnlEdit;

    
    public TableGridEditWindow(TableGrid domain){
        super(domain);
        setMode(EditMode.EDITONLY);
        pnlEdit = new TableGridEditableForm();
        registerEditForm(pnlEdit);  
    }
    
    public TableGridEditWindow(TableGrid domain, Grid<BeanModel> grid){
        super(domain, grid);
        setMode(EditMode.EDITONLY);
        pnlEdit = new TableGridEditableForm();
        registerEditForm(pnlEdit);
    }
    
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);            
        setSize(800, 600);
    }
    
    @Override
    protected void initPagingToolBar() {
        super.initPagingToolBar();
        pagingToolBar.setBorders(false);
    }

    @Override
    protected GWTEditServiceAsync getService() {
        return service;
    }

    @Override
    protected Component getEditPanel() {
        return pnlEdit;
    }

    @Override
    protected Component getViewPanel() {
        return null;
    }

    @Override
    protected String getHeaderCreate() {
        return "";//constants.headerCreateDictionary();
    }

    @Override
    protected String getHeaderViewEdit() {
        return "";//constants.headerEditDictionary();
    }

    @Override
    protected TableGrid createDomain() {
        TableGrid domain =  pnlEdit.createTableGrid();
        return domain;
    }

    @Override
    protected void focusInvalidField() {
        //TODO: add implementation
    }
    
    
}
