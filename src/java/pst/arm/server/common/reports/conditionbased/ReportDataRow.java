package pst.arm.server.common.reports.conditionbased;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Artem Vorontsov
 */
public class ReportDataRow {
    protected List<String> row;

    protected String getValue(Object value) {
        String ret = "";
        if(value != null) {
            ret = value.toString();
        }

        return ret;
    }

    public void add(Object value) {
        if(row == null) {
            row = new ArrayList<String>();
        }
        
        row.add(getValue(value));
    }

    public void setAt(int index, Object value) {
        try {
            row.set(index, getValue(value));
        } catch (IndexOutOfBoundsException e) {

        }
    }

    public String getAt(int index) {
        String ret = null;
        try {
            ret = row.get(index);
        } catch (IndexOutOfBoundsException e) {

        }
        return ret;
    }
    
    public Integer getSize()
    {
        return ((row == null) ? -1 : (Integer)row.size());
    }
}
