package pst.arm.client.common.domain.reports;

import java.io.Serializable;

/**
 *
 * @author vorontsov
*/
public class ColumnDescriptor implements Serializable {
    public String colId;
    public String colName;
    public Float colWidth;
    public Boolean useRenderer;

    public ColumnDescriptor() {
        useRenderer = Boolean.FALSE;
    }
    
    public ColumnDescriptor(String colId, String colName) {
        this();
        
        this.colId = colId;
        this.colName = colName;
    }

    public ColumnDescriptor(String colId, String colName, Float colWidth) {
        this(colId, colName);
        
        this.colWidth = colWidth;
    }

    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
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

    public Boolean getUseRenderer() {
        return useRenderer;
    }

    public void setUseRenderer(Boolean useRenderer) {
        this.useRenderer = useRenderer;
    }
}
