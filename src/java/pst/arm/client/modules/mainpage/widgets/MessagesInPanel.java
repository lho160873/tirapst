package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style;
import pst.arm.client.modules.header.FloatNavigationPanel;
import pst.arm.client.modules.leveltask.widgets.in.LevelTaskInPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MessagesInPanel extends LevelTaskInPanel {
    public MessagesInPanel() {
        super();
        initView();
    }
   
    public MessagesInPanel(FloatNavigationPanel np) {
        super(np);
       initView();
    }
    private void initView()
    {
        this.setIsShowPanelFiltr(Boolean.FALSE);
        this.setIsStartWithShowPanelFiltr(Boolean.FALSE);
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeaderVisible(false);
    }
}
