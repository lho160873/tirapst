/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.datagrid.dao.expansion;

/**
 *
 * @author Igor
 */
public interface DUpdatePlanningExpansionDAO {

    public void updatePlanningSprDepart(Integer interactingSystId, String dbSource);

    public void updatePlanningSprOperkind(Integer interactingSystId, String dbSource);

    public void updateDesign(Integer interactingSystId, String dbSource);

    public void updateDesignVer(Integer interactingSystId, String dbSource);

    public void addDesign(Integer interactingSystId, String dbSource);

    public void addDesignVer(Integer interactingSystId, String dbSource);
}
