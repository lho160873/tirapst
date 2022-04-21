/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.massmailingig.service.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author Igor
 */
@RemoteServiceRelativePath("service/massMailingIgService")
public interface GWTMassMailingIgService extends RemoteService {

    public void addWithCopy(Integer userId, Integer massMailingId);
    
}
