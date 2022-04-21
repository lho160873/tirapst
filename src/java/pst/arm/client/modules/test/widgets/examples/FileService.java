/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.test.widgets.examples;

import java.util.List;

//import com.extjs.gxt.samples.client.examples.model.FileModel;
import pst.arm.client.modules.test.widgets.examples.model.FileModel;
import com.extjs.gxt.ui.client.data.RemoteSortTreeLoadConfig;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Example <code>RemoteService</code>.
 */
public interface FileService extends RemoteService {

  /**
   * Returns the children of the given parent.
   * 
   * @param folder the parent folder
   * @return the children
   */
  public List<FileModel> getFolderChildren(FileModel folder);

  /**
   * Returns the children of the given parent.
   * 
   * @param loadConfig the load config
   * @return the children
   */
  public List<FileModel> getFolderChildren(RemoteSortTreeLoadConfig loadConfig);

}
