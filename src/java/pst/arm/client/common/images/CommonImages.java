package pst.arm.client.common.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Common use images interface
 *
 * @author Alexandr Kozhin
 * @since 0.14.0
 */
public interface CommonImages extends ClientBundle {

    @Source("add.png")
    ImageResource add();

    @Source("accept.png")
    ImageResource accept();

    @Source("cancel.png")
    ImageResource cancel();

    @Source("delete.png")
    ImageResource delete();

    @Source("edit.png")
    ImageResource edit();

    @Source("save.png")
    ImageResource save();

    @Source("save_next.png")
    ImageResource saveNext();

    @Source("save_close.png")
    ImageResource saveClose();

    @Source("view.png")
    ImageResource view();

    @Source("copy.png")
    ImageResource copy();

    @Source("saveclose.png")
    ImageResource saveclose();

    @Source("book.png")
    ImageResource book();

    @Source("check.png")
    ImageResource check();

    @Source("find.png")
    ImageResource find();

    @Source("reset.png")
    ImageResource reset();

    @Source("attention.gif")
    ImageResource attention();

    @Source("attention_out.gif")
    ImageResource attentionOut();

    @Source("attention_in.gif")
    ImageResource attentionIn();

    //@Source("letter.gif")
    @Source("msg_out.gif")
    ImageResource letter();

    //@Source("read_letter.gif")
    @Source("msg_in.gif")
    ImageResource readLetter();

    @Source("sync16.png")
    ImageResource sync();

    @Source("columns_visibility.png")
    ImageResource columnsVisibility();
    
    @Source("columns_visibility_del.png")
    ImageResource columnsVisibilityDel();

    @Source("rotate.png")
    ImageResource rotate();
    
    @Source("tick_red.png")
    ImageResource tick_red();
    
    @Source("tick.png")
    ImageResource tick();
   
    @Source("msg_send.png")
    ImageResource msg_send();

    @Source("msg_rotate.png")
    ImageResource msg_rotate();

    @Source("msg_read.png")
    ImageResource msg_read();

    @Source("msg_close.png")
    ImageResource msg_close();

    @Source("msg_check.png")
    ImageResource msg_check();
    
    @Source("add_file.png")
    ImageResource add_file();

}
