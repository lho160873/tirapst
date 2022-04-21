package pst.arm.server.modules.reportbuilder.object;

import java.io.Serializable;
import pst.arm.client.common.DataUtil;
import pst.arm.client.common.domain.EditableDomain;

public class Row implements Serializable, EditableDomain<Row> {

    private Integer headerSpecId;
    private Integer x;
    private Integer y;
    private Integer lineNumber;
    private String content;
    private Integer fontSize;
    private Integer fat;
    private Integer alignment;

    public Integer getHeaderSpecId() {
        return headerSpecId;
    }

    public void setHeaderSpecId(Integer headerSpecId) {
        this.headerSpecId = headerSpecId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getAlignment() {
        return alignment;
    }

    public void setAlignment(Integer alignment) {
        this.alignment = alignment;
    }

    @Override
    public Row newInstance() {
        return new Row();
    }

    @Override
    public void copy(Row domain) {
        this.headerSpecId = domain.headerSpecId;
        this.x = domain.x;
        this.y = domain.y;
        this.lineNumber = domain.lineNumber;
        this.content = domain.content;
        this.fontSize = domain.fontSize;
        this.fat = domain.fat;
        this.alignment = domain.alignment;
    }

    @Override
    public Boolean isDomainFull() {
        return (this.headerSpecId != null) & (this.x != null) & (this.y != null) & (this.lineNumber != null)
                & (this.content != null) & (this.fontSize != null) & (this.fat != null) & (this.alignment != null);
    }

    @Override
    public Long getDomainId() {
        return this.headerSpecId.longValue() * 1000 + lineNumber.longValue();
    }

    @Override
    public Boolean isDomainEquals(Row domain) {
        return (DataUtil.compare(this.headerSpecId, domain.headerSpecId)) & (DataUtil.compare(this.x, domain.x))
                & (DataUtil.compare(this.y, domain.y)) & (DataUtil.compare(this.lineNumber, domain.lineNumber))
                & (DataUtil.compare(this.content, domain.content)) & (DataUtil.compare(this.fontSize, domain.fontSize))
                & (DataUtil.compare(this.fat, domain.fat)) & (DataUtil.compare(this.alignment, domain.alignment));
    }
}
