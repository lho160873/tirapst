package pst.arm.server.utils;

import org.apache.log4j.Logger;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.reports.ReportGeneratorResult;
import pst.arm.client.modules.datagrid.domain.DDataGrid;
import pst.arm.client.modules.datagrid.domain.IRowColumnVal;
import pst.arm.client.modules.datagrid.domain.SKeyForColumn;
import pst.arm.server.modules.datagrid.service.DDataGridService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Ratmir Slepenkov
 * @version 0.17.0
 */
public class ReportUtils
{
    private final static String DESCRIPTOR = "descriptor";
    private final static String ERRORS = "errors";
    private static Logger log = Logger.getLogger("DDataGridStoreExtractor");
    static final protected String errorUserDoesNotHaveAccess = "Вам недоступен данный отчет";
    
    /**
     * <p>Parse results received from business service.<p>
     * <p>If no errors put FileObjectDescriptor into session and return ReportGeneratorResult
     * with UUID of report in session</p>
     * <p>If errors while report generation return ReportGeneratorResult with errors list and no UUID</p>
     * @param serviceResult map contains two elements "descriptor" - FileObjectDescriptor with generated report and
     * "errors" - List<String> with error messages
     * @param session session to put FileObjectDescriptor with report
     * @return ReportGeneratorResult with report id in session if success or the same object with errors list if fail. 
     */
    public static ReportGeneratorResult putReportIntoSession(Map<String, Object> serviceResult, HttpSession session){
        ReportGeneratorResult result = new ReportGeneratorResult();

        FileObjectDescriptor fod = (FileObjectDescriptor) serviceResult.get(DESCRIPTOR);
        result.setMessages((List<String>) serviceResult.get(ERRORS));

        if (fod != null && result.isSuccess()) {
            String uuid = UUID.randomUUID().toString();
            session.setAttribute(uuid, fod);

            result.setId(uuid);
        }

        return result;
    }
    
    /**
     * Put report into session.
     * @param fod report descriptor
     * @param session 
     * @return if descriptor is not null return ReportGeneratorResult with uuid of
     * report descriptor into session else return empty ReportGeneratorResult.
     */
    public static ReportGeneratorResult putReportIntoSession(FileObjectDescriptor fod, HttpSession session){
        ReportGeneratorResult result = new ReportGeneratorResult();
        if(fod != null){
            String uuid = UUID.randomUUID().toString();
            session.setAttribute(uuid, fod);
            result.setId(uuid);
        }        
        return result;
    }
    
    /**
     * Put report into session.
     * @param fod report descriptor
     * @param session 
     * @return if descriptor is not null return ReportGeneratorResult with uuid of
     * report descriptor into session else return empty ReportGeneratorResult.
     */
    public static ReportGeneratorResult putReportIntoSessionWithAccessControl(FileObjectDescriptor fod, HttpSession session,
            Integer id, Integer[] rightRoles, DDataGridService service)
    {
        ReportGeneratorResult result = new ReportGeneratorResult();
        if (fod != null)
        {
            String uuid = UUID.randomUUID().toString();
            session.setAttribute(uuid, fod);
            result.setId(uuid);
        }
        List<DDataGrid> table = service.getAllDataGrid("USER_ROLES");
        Boolean isCorrectId = false;
        for (int i = 0; i < table.size(); i++)
        {
            HashMap<SKeyForColumn,IRowColumnVal> row = table.get(i).getRows();
            if (((Integer)(row.get(new SKeyForColumn("MAIN", "USER_ID")).getVal())).equals(id))
            {
                for (int j = 0; j < rightRoles.length; j++)
                {
                    if (((Integer)(row.get(new SKeyForColumn("MAIN", "ROLE_ID")).getVal())).equals(rightRoles[j]))
                    {
                        isCorrectId = true;
                        break;
                    }
                }
            }
        }
        if (!isCorrectId)
        {
            result.addMessage(errorUserDoesNotHaveAccess);
        }
        return result;
    }
    
    /**
     * Create result what returns by business service after generation method.
     * @param fod
     * @param errors
     * @return map contains two elements "descriptor" - FileObjectDescriptor with generated report and
     * "errors" - List<String> with error messages
     */
    public static Map<String, Object> createResultMap(FileObjectDescriptor fod, List<String> errors) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(DESCRIPTOR, fod);
        result.put(ERRORS, errors);

        return result;
    }
}
