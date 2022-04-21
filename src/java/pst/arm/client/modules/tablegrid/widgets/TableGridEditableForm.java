package pst.arm.client.modules.tablegrid.widgets;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import pst.arm.client.common.ui.form.EditableForm;
import pst.arm.client.modules.tablegrid.domain.TableGrid;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class TableGridEditableForm extends LayoutContainer implements EditableForm<TableGrid>
{
    private TextField<String> tfName;
    private NumberField nfId;
    
    public TableGridEditableForm(){
        super();
        //initListeners();
        initComponents();
        arrangeComponents();

    }
    
    
    @Override
    public Boolean validate()
    {
        return true;
        //throw new UnsupportedOperationException("Not supported yet.");
    }
     
    @Override
    public void clear()
    {
        //throw new UnsupportedOperationException("Not supported yet.");
         tfName.setValue("");
         nfId.setValue(-1);
    }
 
    @Override
    public void setDomain(TableGrid object)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
         if(object == null)
            return;
        tfName.setValue(object.getName());
        nfId.setValue(object.getId());
    }
    
    @Override
    public void fillDomain(TableGrid object)
    {
        //throw new UnsupportedOperationException("Not supported yet.");  
        object.setName(tfName.getValue());
        object.setId(nfId.getValue().longValue());
       
    }
    
    public TableGrid createTableGrid() 
    {        
        TableGrid d = new TableGrid();
       
        long id = nfId.getValue().longValue();
        d.setId(id);
        d.setName(tfName.getValue()); 
        
        return d;
    }
    
    private void initComponents()
    {
        nfId = new NumberField();       
        nfId.setFieldLabel("Код");
        
        tfName = new TextField<String>();
        tfName.setAllowBlank(false);
        tfName.setFieldLabel("Название");
        tfName.setMaxLength(150);
               
    }
    
    
    private void arrangeComponents(){
              //setLayout(new RowLayout(Orientation.VERTICAL));
        FormData formData = new FormData("100%");  
     
     //VerticalPanel vp = new VerticalPanel();  
     //vp.setSpacing(10);  
     FieldSet simple = new FieldSet();  
     //simple.setHeading("Основные данные"); 
     simple.setBorders(false);
     //simple.setHeaderVisible(false); 
     //simple.setFrame(false);  
     //simple.setWidth(350);  
     simple.setLayout(new FormLayout());
  
     simple.add(nfId, formData);  
     simple.add(tfName, formData);  
       add(simple); 
/*        setStyleAttribute("padding", "5px");
        setLayout(new RowLayout(Orientation.VERTICAL));
        
        FormData fd = new FormData("100%");
        fd.setMargins(new Margins(0, 16, 0, 0));
        
        //add top         
        FieldSet fstDictDetails = new FieldSet();           
        fstDictDetails.setLayout(new FormLayout()); 
        
        fstDictDetails.add(tfName, fd);
        fstDictDetails.add(nfId, fd);
        //add(fstDictDetails, new RowData(1,-1));
        add(fstDictDetails);
*/       
    }    
}
