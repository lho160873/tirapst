package pst.arm.client.common.domain;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 *
 * @author s_smirnov
*/
public class PortletLink extends BaseModelData{
    private String text;
    private String href;

    /**
     * @return the text
     */
    public String getText() {
        return get("text");
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        set("text", text);
    }

    /**
     * @return the href
     */
    public String getHref() {
        return get("href");
    }

    /**
     * @param href the href to set
     */
    public void setHref(String href) {
        set("href", href);
    }
}
