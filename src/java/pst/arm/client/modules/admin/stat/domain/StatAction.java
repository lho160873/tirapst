package pst.arm.client.modules.admin.stat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kozhin
 */
public class StatAction implements Serializable {

    private String type = null;  //can't be null
    private String name = null;
    private String fullUrl = null;
    private String url = null;
    private String urlOptions = null;
    private String strDate = null;
    private Date date = null;

    public StatAction() {
    }

    public StatAction(String date, String type, String name, String url) {
        this.strDate = date;
        this.type = type;
        this.name = name;
        this.fullUrl = url;
        parseUrl();
    }

    protected void parseUrl()
    {
        if( this.type.equals("PAGE"))
        {
            String[] parts = this.fullUrl.split("/");
            this.url = parts[parts.length-1];
        }else if( this.type.equals("SERVICE"))
        {
            //substr GWT from begin ang Impl from end
            this.url = this.name.substring(3,this.name.length()-4);
        }
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
            this.url = url.split("&")[0];
    }

    /**
     * @return the date
     */
    public String getStrDate() {
        return strDate;
    }

    /**
     * @param date the date to set
     */
    public void setStrDate(String date) {
        this.strDate = date;
        //this.date = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss").parse(date);
    }

    /**
     * @return the urlOptions
     */
    public String getUrlOptions() {
        return urlOptions;
    }

    /**
     * @param urlOptions the urlOptions to set
     */
    public void setUrlOptions(String urlOptions) {
        this.urlOptions = urlOptions;
    }

    /**
     * @return the fullUrl
     */
    public String getFullUrl() {
        return fullUrl;
    }

    /**
     * @param fullUrl the fullUrl to set
     */
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
