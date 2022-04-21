package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import pst.arm.client.modules.header.FloatNavigationPanel;
import pst.arm.client.modules.leveltask.widgets.out.LevelTaskPanel;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MessagesOutPanel extends LevelTaskPanel {

    public MessagesOutPanel() {
        super();
        initView();
    }

    public MessagesOutPanel(FloatNavigationPanel np, String currentId) {
        super(np, currentId);
        initView();
    }

    private void initView() {
        this.setIsShowPanelFiltr(Boolean.FALSE);
        this.setIsStartWithShowPanelFiltr(Boolean.FALSE);
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeaderVisible(false);
    }
}

