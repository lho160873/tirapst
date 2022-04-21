package pst.arm.server.common.reports.conditionbased;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Artem Vorontsov
 */
public class ReportColumns {
    protected List<ReportColumnsDescriptor> columns;

    public ReportColumns() {
        columns = new ArrayList<ReportColumnsDescriptor>(10);
    }

    public void addColumn(String columnName) {
        columns.add(new ReportColumnsDescriptor(columnName));
    }

    public void addColumn(String columnName, float width) {
        columns.add(new ReportColumnsDescriptor(columnName, width));
    }

    public void addColumn(ReportColumnsDescriptor cd) {
        columns.add(cd);
    }


    public void setColumnAtPos(int index, String columnName) {
        columns.set(index, new ReportColumnsDescriptor(columnName));
    }

    public void setColumnAt(int index, String columnName, float width) {
        columns.add(index, new ReportColumnsDescriptor(columnName, width));
    }

    public void setColumnAt(int index, ReportColumnsDescriptor columnDescriptor) {
        columns.add(index, columnDescriptor);
    }

    public ReportColumnsDescriptor getColumnAt(int index) {
        ReportColumnsDescriptor ret = null;

        try {
            ret = columns.get(index);

        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        return ret;
    }

    public String getColumnName(int index) {
        ReportColumnsDescriptor column = getColumnAt(index);
        if(column == null) {
            return null;
        }
        return column.getColName();
    }

    public Float getColumnWidth(int index) {
        ReportColumnsDescriptor column = getColumnAt(index);
        if(column == null) {
            return null;
        }
        return column.getColWidth();
    }

    public Integer getColumnCount() {
        if(columns == null) {
            return null;
        }

        return columns.size();
    }
}
