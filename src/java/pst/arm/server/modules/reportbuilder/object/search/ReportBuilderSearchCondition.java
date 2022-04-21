package pst.arm.server.modules.reportbuilder.object.search;

import pst.arm.client.common.domain.search.SearchConditionSimple;

public class ReportBuilderSearchCondition extends SearchConditionSimple {

    private Integer headerSpecId;
    private Integer lineNumber;
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getHeaderSpecId() {
        return headerSpecId;
    }

    public void setHeaderSpecId(Integer headerSpecId) {
        this.headerSpecId = headerSpecId;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
}
