package pst.arm.server.common.reports.conditionbased;

/**
 *
 * @author Mikhail Zakharov
 */
public class ReportKpTariffs extends Report
{
    private String info;

    public ReportKpTariffs() 
    {
        caption = "";
        text = "";
        info = "";
        timeStamp = Boolean.FALSE;
        vertical = Boolean.FALSE;
        disableCopy = Boolean.TRUE;
        addSearchPanel = Boolean.TRUE;
        reportType = ReportType.RTF;
        columns = new ReportColumns();
        data = new ReportData();
    }

    public ReportKpTariffs(String caption)
    {
        this();
        
        this.caption = caption;
    }

    public void setInfo(String var)
    {
        info = var;
    }
    
    public String getInfo()
    {
        return info;
    }
}