package pst.arm.client.common.domain.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for use search conditions
 *
 * @author Alexandr Kozhin
 */
public class SearchCondition implements Serializable {
    private Long conditionId;
    private Integer archiveId;
    private Integer recId;
    private Integer offset = 0;
    private Integer limit = 50;
    private String sortDir = "NONE";
    private String sortField;
    private Integer rowFrom;
    private Integer rowTo;
    private String query;
    private Integer yearFrom;
    private Integer yearTo;
    private boolean caseSensitive = false;
    private boolean wordBegin = false;
    private boolean multiArchive = false;
    private List<String> queryWords;
    private List<Long> recIds;
    private Boolean isGridOnly = Boolean.TRUE;
    private Boolean useStemming = Boolean.TRUE;
    private Boolean useMarkup = Boolean.TRUE;
    
    public SearchCondition(){}

    public SearchCondition(Integer archiveId, String query, Integer yearFrom, Integer yearTo) {
        this.archiveId = archiveId;
        this.query = query;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;

        handleQuery();
    }
    
    public SearchCondition(SearchCondition condition) {
        conditionId = condition.conditionId;
        archiveId = condition.archiveId;
        recId = condition.recId;
        offset = condition.offset;
        limit = condition.limit;
        sortDir = condition.sortDir;
        sortField = condition.sortField;
        rowFrom = condition.rowFrom;
        rowTo = condition.rowTo;
        query = condition.query;
        yearFrom = condition.yearFrom;
        yearTo = condition.yearTo;
        caseSensitive = condition.caseSensitive;
        wordBegin = condition.wordBegin;
        multiArchive = condition.multiArchive;
        isGridOnly = condition.isGridOnly;
        useStemming = condition.useStemming;
        useMarkup = condition.useMarkup;
        
        if(condition.queryWords == null) {
            queryWords = condition.queryWords;
        } else {
            queryWords = new ArrayList<String>(condition.queryWords.size());
            for(String queryWord : condition.queryWords) {
                queryWords.add(queryWord);
            }
        }
        
        if(condition.recIds == null) {
            recIds = condition.recIds;
        } else {
            recIds = new ArrayList<Long>(condition.recIds.size());
            for(Long id : condition.recIds) {
                recIds.add(id);
            }
        }
    }

    public boolean hasId() {
        return (this.recId != null);
    }

    public boolean hasIds() {
        return ((this.recIds != null) && (!this.recIds.isEmpty()));
    }

    private void handleQuery() {
        if (this.query != null && !this.query.isEmpty()) {
            this.setQueryWords(new ArrayList<String>());
            this.getQueryWords().addAll(Arrays.asList(this.query.trim().split("\\s+")));
        }
    }

    /**
     * @return the archiveId
     */
    public Integer getArchiveId() {
        return archiveId;
    }

    /**
     * @param archiveId the archiveId to set
     */
    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
        handleQuery();
    }

    /**
     * @return the yearFrom
     */
    public Integer getYearFrom() {
        return yearFrom;
    }

    /**
     * @param yearFrom the yearFrom to set
     */
    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }

    /**
     * @return the yearTo
     */
    public Integer getYearTo() {
        return yearTo;
    }

    /**
     * @param yearTo the yearTo to set
     */
    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }

    /**
     * @return the rowFrom
     */
    public Integer getRowFrom() {
        return rowFrom;
    }

    /**
     * @param rowFrom the rowFrom to set
     */
    public void setRowFrom(Integer rowFrom) {
        this.rowFrom = rowFrom;
    }

    /**
     * @return the rowTo
     */
    public Integer getRowTo() {
        return rowTo;
    }

    /**
     * @param rowTo the rowTo to set
     */
    public void setRowTo(Integer rowTo) {
        this.rowTo = rowTo;
    }

    /**
     * @return the pagingConfig
     */
//    public PagingLoadConfig getPagingConfig() {
//        return pagingConfig;
//    }

    public void setPagingConfig(Integer limit, Integer offset, String sortDir, String sortField) {
        setLimit(limit);
        setOffset(offset);
        setSortDir(sortDir);
        setSortField(sortField);
        setRowFrom(offset);
        setRowTo(offset + limit);
    }
    
    public void setPagingConfig(Integer limit, Integer offset) {
        setPagingConfig(limit, offset, sortDir, sortField);
    }    

    public Integer getOffset() {
        return offset;
    }

    public Integer getOffsetEnd() {
        return (getOffset() + getLimit());
    }

    public Integer getLimit() {
        return limit;
    }

    /**
     * @return the caseSensitive
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * @param caseSensitive the caseSensitive to set
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * @return the wordBegin
     */
    public boolean isWordBegin() {
        return wordBegin;
    }

    /**
     * @param wordBegin the wordBegin to set
     */
    public void setWordBegin(boolean wordBegin) {
        this.wordBegin = wordBegin;
    }

    /**
     * @return the queryWords
     */
    public List<String> getQueryWords() {
        return queryWords;
    }

    /**
     * @param queryWords the queryWords to set
     */
    public void setQueryWords(List<String> queryWords) {
        this.queryWords = queryWords;
    }

    /**
     * @return the id
     */
    public Integer getRecId() {
        return recId;
    }

    /**
     * @param id the id to set
     */
    public void setRecId(Integer id) {
        this.recId = id;
    }

    /**
     * @return the id
     */
    public Long getConditionId() {
        return conditionId;
    }

    /**
     * @param id the id to set
     */
    public void setConditionId(Long id) {
        this.conditionId = id;
    }

    

    public void addRecId(Long recId) {
        if(recIds == null) {
            recIds = new ArrayList<Long>();
        }

        recIds.add(recId);
    }


    /**
     * @return the recIds
     */
    public List<Long> getRecIds() {
        return recIds;
    }

    /**
     * @param recIds the recIds to set
     */
    public void setRecIds(List<Long> recIds) {
        this.recIds = recIds;
    }

    /**
     * @return the isGridOnly
     */
    public Boolean isGridOnly() {
        return isGridOnly;
    }

    /**
     * @param isGridOnly the isGridOnly to set
     */
    public void setIsGridOnly(Boolean isGridOnly) {
        this.isGridOnly = isGridOnly;
    }

    /**
     * @return the useStemming
     */
    public Boolean getUseStemming() {
        return useStemming;
    }

    /**
     * @param useStemming the useStemming to set
     */
    public void setUseStemming(Boolean useStemming) {
        this.useStemming = useStemming;
    }

    /**
     * @return the useMarkup
     */
    public Boolean getUseMarkup() {
        return useMarkup;
    }

    /**
     * @param useMarkup the useMarkup to set
     */
    public void setUseMarkup(Boolean useMarkup) {
        this.useMarkup = useMarkup;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @return the sortDir
     */
    public String getSortDir() {
        return sortDir;
    }

    /**
     * @param sortDir the sortDir to set
     */
    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    /**
     * @return the sortField
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * @param sortField the sortField to set
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isMultiArchive() {
        return multiArchive;
    }

    public void setMultiArchive(boolean multiArchive) {
        this.multiArchive = multiArchive;
    }
}
