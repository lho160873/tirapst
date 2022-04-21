package pst.arm.server.modules.reportbuilder.dao;

import java.util.List;
import pst.arm.server.modules.reportbuilder.object.Row;
import pst.arm.server.modules.reportbuilder.object.search.ReportBuilderSearchCondition;

public interface ReportBuilderDAO {

    public List<Row> getAllRows();

    public List<Row> getRows(ReportBuilderSearchCondition condition);
}
