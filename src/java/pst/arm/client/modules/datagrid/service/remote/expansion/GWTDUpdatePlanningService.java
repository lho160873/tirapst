/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author Igor
 */
@RemoteServiceRelativePath("service/dupdatePlanningExpansionService")
public interface GWTDUpdatePlanningService extends RemoteService {

    public void updatePlanningSprDepart(Integer interactingSystId, String dbSource);

    public void updatePlanningSprOperkind(Integer interactingSystId, String dbSource);

    public void updateDesign(Integer interactingSystId, String dbSource);

    public void updateDesignVer(Integer interactingSystId, String dbSource);

    public void addDesign(Integer interactingSystId, String dbSource);

    public void addDesignVer(Integer interactingSystId, String dbSource);
}
