package pst.arm.client.modules.datagrid.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface DataGridImages extends ClientBundle {

    @Source("book.png")
    ImageResource book();

    @Source("cancel.png")
    ImageResource cancel();

    @Source("check.png")
    ImageResource check();

    @Source("find.png")
    ImageResource find();

    @Source("form.png")
    ImageResource form();

    @Source("reset.png")
    ImageResource reset();

    @Source("view.png")
    ImageResource view();
    
    @Source("help_manual.png")
    ImageResource helpmanual();
    
    /*@Source("page_white_add.gif")
    ImageResource openDataGrid();
    
    @Source("move-top.gif")
    ImageResource up();
    
    @Source("move-bottom.gif")
    ImageResource down();
    
    @Source("page_white_add.png")
    ImageResource loadFromFile();*/
}
