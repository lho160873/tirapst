package pst.arm.client.common.ui.grid;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import java.io.Serializable;
import pst.arm.client.common.AppHelper;

/**
 * GridConfig is a class to configure different grid components. This class 
 * allows to specify the following options : 
 * <ul type=square>
 *  <li><i>pageSize</i> - </li>
 *  <li><i>autoExpandColumn</i> - </li>
 *  <li><i>autoLoad</i> - </li>
 *  <li><i>topToolbarVisible</i> - </li>
 *  <li><i>topToolbarSpacing</i> - </li>
 *  <li><i>htmlPreviewEnable</i> - </li>
 *  <li><i>saveEnable</i> - </li>
 *  <li><i>pagingToolbarVisible</i> - </li>
 * </ul>
 *
 * @author Alexandr Kozhin
 * @since 0.5.0
 */
public class GridConfig implements Serializable{

    /**
     * Grid settings
     */
    //Rows count per one grid page
    private Integer pageSize = Integer.parseInt(AppHelper.getInstance().getConfiguration().pageRecordCount());
    private String autoExpandColumn = null;
    private Boolean autoLoad = Boolean.TRUE;

    /**
     * Toolbar settings
     */
    private Boolean topToolbarVisible = Boolean.TRUE;
    private Integer topToolbarSpacing = 3;
    private Boolean htmlPreviewEnable = Boolean.TRUE;
    private Boolean saveAllResultsEnable = Boolean.TRUE;
    private Boolean viewAllResultsEnable = Boolean.TRUE;
    private Boolean saveSelectedResultsEnable = Boolean.TRUE;
    private Boolean isFullContentEnabled = Boolean.TRUE;

    private Boolean pagingToolbarVisible = Boolean.TRUE;

    private SelectionMode selectionMode = SelectionMode.MULTI;

    public GridConfig() {
    }

    /**
     * @return the pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the autoExpandColumn
     */
    public String getAutoExpandColumn() {
        return autoExpandColumn;
    }

    /**
     * @param autoExpandColumn the autoExpandColumn to set
     */
    public void setAutoExpandColumn(String autoExpandColumn) {
        this.autoExpandColumn = autoExpandColumn;
    }

    /**
     * @return the autoLoad
     */
    public Boolean isAutoLoad() {
        return autoLoad;
    }

    /**
     * @param autoLoad the autoLoad to set
     */
    public void setAutoLoad(Boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    /**
     * @return the topToolbarVisible
     */
    public Boolean getTopToolbarVisible() {
        return topToolbarVisible;
    }

    /**
     * @param topToolbarVisible the topToolbarVisible to set
     */
    public void setTopToolbarVisible(Boolean topToolbarVisible) {
        this.topToolbarVisible = topToolbarVisible;
    }

    /**
     * @return the topToolbarSpacing
     */
    public Integer getTopToolbarSpacing() {
        return topToolbarSpacing;
    }

    /**
     * @param topToolbarSpacing the topToolbarSpacing to set
     */
    public void setTopToolbarSpacing(Integer topToolbarSpacing) {
        this.topToolbarSpacing = topToolbarSpacing;
    }

    /**
     * @return the pagingToolbarVisible
     */
    public Boolean getPagingToolbarVisible() {
        return pagingToolbarVisible;
    }

    /**
     * @param pagingToolbarVisible the pagingToolbarVisible to set
     */
    public void setPagingToolbarVisible(Boolean pagingToolbarVisible) {
        this.pagingToolbarVisible = pagingToolbarVisible;
    }

    /**
     * @return the htmlPreviewEnable
     */
    public Boolean isHtmlPreviewEnable() {
        return htmlPreviewEnable;
    }

    /**
     * @param htmlPreviewEnable the htmlPreviewEnable to set
     */
    public void setHtmlPreviewEnable(Boolean htmlPreviewEnable) {
        this.htmlPreviewEnable = htmlPreviewEnable;
    }

    /**
     * @return the saveEnable
     */
    public Boolean isSaveSelectedResultsEnable() {
        return saveSelectedResultsEnable;
    }

    /**
     * @param saveEnable the saveEnable to set
     */
    public void setSaveSelectedResultsEnable(Boolean saveEnable) {
        this.saveSelectedResultsEnable = saveEnable;
    }

    /**
     * @return the selectionMode
     */
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    /**
     * @param selectionMode the selectionMode to set
     */
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    /**
     * @return the reportEnable
     */
    public Boolean isSaveAllResultsEnable() {
        return saveAllResultsEnable;
    }

    /**
     * @param reportEnable the reportEnable to set
     */
    public void setSaveAllResultsEnable(Boolean reportEnable) {
        this.saveAllResultsEnable = reportEnable;
    }

    public Boolean isViewAllResultsEnable() {
        return viewAllResultsEnable;
    }

    public void setViewAllResultsEnable(Boolean allResults) {
        this.viewAllResultsEnable = allResults;
    }
    
    boolean isFullContentEnabled() {
        return this.isFullContentEnabled;
    }

    /**
     * @param isFullContentEnabled the isFullContentEnabled to set
     */
    public void setIsFullContentEnabled(Boolean isFullContentEnabled) {
        this.isFullContentEnabled = isFullContentEnabled;
    }
}
