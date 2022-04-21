package pst.arm.server.modules.reportbuilder.service;

import java.util.List;
import pst.arm.server.modules.reportbuilder.object.Row;
import pst.arm.server.modules.reportbuilder.object.search.ReportBuilderSearchCondition;

public interface ReportBuilderService {

    public List<Row> getAllRows();

    public List<Row> getRows(ReportBuilderSearchCondition condition);
}
