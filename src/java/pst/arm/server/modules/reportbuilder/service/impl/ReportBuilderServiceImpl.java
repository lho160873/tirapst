package pst.arm.server.modules.reportbuilder.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.server.modules.reportbuilder.dao.ReportBuilderDAO;
import pst.arm.server.modules.reportbuilder.object.Row;
import pst.arm.server.modules.reportbuilder.object.search.ReportBuilderSearchCondition;
import pst.arm.server.modules.reportbuilder.service.ReportBuilderService;

@Service
public class ReportBuilderServiceImpl implements ReportBuilderService {

    private ReportBuilderDAO reportBuilderDAO;
    private static Logger log = Logger.getLogger("ReportBuilderServiceImpl");

    @Autowired
    public void setReportBuilderDAO(ReportBuilderDAO reportBuilderDAO) {
        this.reportBuilderDAO = reportBuilderDAO;
    }

    @Override
    public List<Row> getAllRows() {
        return reportBuilderDAO.getAllRows();
    }

    @Override
    public List<Row> getRows(ReportBuilderSearchCondition condition) {
        return reportBuilderDAO.getRows(condition);
    }
}
