package pst.arm.server.common.reports.conditionbased;

/**
 *
 * @author Artem Vorontsov
 */
public class Report {

    public enum ReportType {
        RTF, PDF, HTML, XLS, CSV
    }

    protected Long id;
    protected String caption;
    protected String text;
    protected Boolean timeStamp;
    protected Boolean vertical;
    protected Boolean disableCopy;
    protected Boolean addSearchPanel;
    protected ReportColumns columns;
    protected ReportData data;
    protected ReportType reportType;

    public Report() {
        caption = "";
        text = "";
        timeStamp = Boolean.FALSE;
        vertical = Boolean.FALSE;
        disableCopy = Boolean.TRUE;
        addSearchPanel = Boolean.TRUE;
        reportType = ReportType.RTF;
        columns = new ReportColumns();
        data = new ReportData();
    }

    public Report(String caption) {
        this();
        
        this.caption = caption;
    }


    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the timeStamp
     */
    public Boolean getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(Boolean timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the vertical
     */
    public Boolean getVertical() {
        return vertical;
    }

    /**
     * @param vertical the vertical to set
     */
    public void setVertical(Boolean vertical) {
        this.vertical = vertical;
    }

    /**
     * @return the columns
     */
    public ReportColumns getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(ReportColumns columns) {
        this.columns = columns;
    }

    /**
     * @return the data
     */
    public ReportData getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ReportData data) {
        this.data = data;
    }

    /**
     * @return the reportType
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * @param reportType the reportType to set
     */
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
    
    public Boolean getDisableCopy() {
        return disableCopy;
    }

    public void setDisableCopy(Boolean disableCopy) {
        this.disableCopy = disableCopy;
    }

    public Boolean getAddSearchPanel() {
        return addSearchPanel;
    }

    public void setAddSearchPanel(Boolean addSearchPanel) {
        this.addSearchPanel = addSearchPanel;
    }
}