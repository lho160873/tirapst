package pst.arm.server.common.reports.conditionbased;

/**
 *
 * @author Artem Vorontsov
 */
public class ReportColumnsDescriptor {
    public String colName;
    public Float colWidth;

    public ReportColumnsDescriptor(String colName) {
        this.colName = colName;
    }

    public ReportColumnsDescriptor(String colName, float colWidth) {
        this(colName);

        this.colWidth = colWidth;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Float getColWidth() {
        return colWidth;
    }

    public void setColWidth(Float colWidth) {
        this.colWidth = colWidth;
    }
}
