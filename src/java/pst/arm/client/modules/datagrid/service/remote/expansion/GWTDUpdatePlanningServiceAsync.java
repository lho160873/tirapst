/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.datagrid.service.remote.expansion;

import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.modules.datagrid.domain.DDataGrid;

/**
 *
 * @author Igor
 */
public interface GWTDUpdatePlanningServiceAsync {

    public void updatePlanningSprDepart(Integer interactingSystId, String dbSource, AsyncCallback<DDataGrid> callback);

    public void updatePlanningSprOperkind(Integer interactingSystId, String dbSource, AsyncCallback<DDataGrid> callback);

    public void updateDesign(Integer interactingSystId, String dbSource, AsyncCallback<DDataGrid> callback);

    public void updateDesignVer(Integer interactingSystId, String dbSource, AsyncCallback<DDataGrid> callback);

    public void addDesign(Integer interactingSystId, String dbSource, AsyncCallback<DDataGrid> callback);

    public void addDesignVer(Integer interactingSystId, String dbSource, AsyncCallback<DDataGrid> callback);
}
