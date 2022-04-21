package pst.arm.client.modules.mainpage.widgets;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class MessagesReglamentPanel extends ContentPanel {

    public MessagesReglamentPanel() {
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeaderVisible(false);
        Html htmlChangeLog = new Html();
        htmlChangeLog.setHtml("<TABLE BORDER=1 WIDTH=500 BGCOLOR=white>"
                + "<TR>"
                + "<TD>Дата</TD>"
                + "<TD>Время</TD>"
                + "<TD>Проблема(тип сообщения)</TD>"
                + "<TD>ID Дела</TD>"
                + "</TR>"
                + "<TR>"
                + "<TD>&nbsp;</TD>"
                + "<TD>&nbsp;</TD>"
                + "<TD>&nbsp;</TD>"
                + "<TD>&nbsp;</TD>"
                + "</TR>"
                + "<TR>"
                + "<TD>&nbsp;</TD>"
                + "<TD>&nbsp;</TD>"
                + "<TD>&nbsp;</TD>"
                + "<TD>&nbsp;</TD>"
                + "</TR>"
                + "</TABLE>");
        add(htmlChangeLog);
    }
}

