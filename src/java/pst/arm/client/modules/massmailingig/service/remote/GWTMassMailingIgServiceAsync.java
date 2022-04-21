/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.massmailingig.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import pst.arm.client.modules.datagrid.domain.DDataGrid;

/**
 *
 * @author Igor
 */
public interface GWTMassMailingIgServiceAsync {

    public void addWithCopy(Integer userId, Integer massMailingId, AsyncCallback<DDataGrid> callback);
    
}
