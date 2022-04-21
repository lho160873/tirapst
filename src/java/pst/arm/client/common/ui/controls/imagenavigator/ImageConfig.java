package pst.arm.client.common.ui.controls.imagenavigator;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseModelData;
import java.io.Serializable;

/**
 *
 * @author s_smirnov
 */
public class ImageConfig extends BaseModel implements Serializable{
    private String id;
    private String url;
    private String title;
    private String previewUrl;
    private String previewTitle;
    //Contains image as string
    private String source;
    
    public ImageConfig() {
    }

    public ImageConfig(String id, String url, String title, String previewUrl, String previewTitle) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setPreviewUrl(previewUrl);
        setPreviewTitle(previewTitle);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     * @param previewUrl the previewUrl to set
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /**
     * @return the previewTitle
     */
    public String getPreviewTitle() {
        return previewTitle;
    }

    /**
     * @param previewTitle the previewTitle to set
     */
    public void setPreviewTitle(String previewTitle) {
        this.previewTitle = previewTitle;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
}
