package pst.arm.client.common.ui.grid;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * GridColumnConfig is a wrapper of gxt ColumnConfig class with many different
 * constructors.
 *
 * @author Alexandr Kozhin
 * @since 0.5.0
 */
public class GridColumnConfig extends ColumnConfig {
    
    private Boolean parentColumn = Boolean.FALSE;

    /**
     * Default constructor
     */
    public GridColumnConfig() {
        super();
    }

    /**
     * Simple constructor with id and lable params
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     */
    public GridColumnConfig(String id, String header) {
        super();
        setId(id);
        setHeader(header);
        setResizable(Boolean.FALSE);
        setMenuDisabled(Boolean.TRUE);
        setAlignment(HorizontalAlignment.CENTER);
    }
    
    public GridColumnConfig(String id, String header, Integer width) {
        super();
        setId(id);
        setHeader(header);
        setWidth(width);
        setFixed(Boolean.TRUE);
        setResizable(Boolean.FALSE);
        setMenuDisabled(Boolean.TRUE);
        setAlignment(HorizontalAlignment.CENTER);
        setSortable(Boolean.FALSE);
    }

    /**
     * Extend constructor to set all column options without renderer
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     * @param width Column width in pixels
     * @param resizeble Resizeble option
     * @param alignment Header text alignment
     */
    public GridColumnConfig(String id, String header, Integer width, Boolean resizeble, HorizontalAlignment alignment) {
        super(id, header, width);
        setResizable(resizeble);
        setAlignment(alignment);
        setMenuDisabled(Boolean.TRUE);
    }

    /**
     * Extend constructor to set all column options without renderer
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     * @param width Column width in pixels
     * @param resizeble Resizeble option
     * @param alignment Header text alignment
     */
    public GridColumnConfig(String id, String header, Integer width, Boolean resizeble, Boolean fixed, HorizontalAlignment alignment) {
        this(id, header, width, resizeble, alignment);
        setFixed(fixed);
    }

    /**
     * Extend constructor to set all column options
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     * @param width Column width in pixels
     * @param resizeble Resizeble option
     * @param alignment Header text alignment
     * @param renderer Column data renderer
     */
    public GridColumnConfig(String id, String header, Integer width, Boolean resizeble, HorizontalAlignment alignment, GridCellRenderer renderer) {
        super(id, header, width);
        setResizable(resizeble);
        setAlignment(alignment);
        setRenderer(renderer);
        setMenuDisabled(Boolean.TRUE);
    }
    
    /**
     * Extend constructor to set all column options
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     * @param width Column width in pixels
     * @param resizeble Resizeble option
     * @param sortable Sortable option
     * @param alignment Header text alignment
     * @param renderer Column data renderer
     */
    public GridColumnConfig(String id, String header, Integer width, Boolean resizeble, Boolean fixed, HorizontalAlignment alignment, GridCellRenderer renderer) {
        this(id, header, width, resizeble, alignment, renderer);
        setFixed(fixed);
    }

    /**
     * Extend constructor to set all column options
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     * @param width Column width in pixels
     * @param resizeble Resizeble option
     * @param sortable Sortable option
     * @param alignment Header text alignment
     * @param renderer Column data renderer
     */
    public GridColumnConfig(String id, String header, Integer width, Boolean resizeble, HorizontalAlignment alignment, Boolean sortable, GridCellRenderer renderer) {
        this(id, header, width, resizeble, alignment, renderer);
        setSortable(sortable);
    }

    /**
     * Constructor for parent columns, that can be hidden.
     *
     * @param id Uniq column identifier
     * @param header Column visible label
     * @param width Column width in pixels
     * @param resizeble Resizeble option
     * @param alignment Header text alignment
     * @param parentCol Option for hidden
     */
    GridColumnConfig(String id, String header, int width, Boolean resizeble, HorizontalAlignment alignment, Boolean parentCol) {
        super(id, header, width);
        setResizable(resizeble);
        setAlignment(alignment);
        setMenuDisabled(Boolean.TRUE);
        setParentColumn(parentCol);
        
    }

    public GridColumnConfig(String id, String header, String dateformat, Integer width, Boolean resizeble, HorizontalAlignment alignment) {
        super(id, header, width);
        setResizable(resizeble);
        setAlignment(alignment);
        setMenuDisabled(Boolean.TRUE);
        setDateTimeFormat(DateTimeFormat.getFormat(dateformat));
    }
    
    /**
     * @return the parentColumn
     */
    public Boolean isParentColumn() {
        return parentColumn;
    }

    /**
     * @param parentColumn the parentColumn to set
     */
    public void setParentColumn(Boolean parentColumn) {
        this.parentColumn = parentColumn;
    }
}
