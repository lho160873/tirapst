/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.test.widgets.examples;

import java.util.List;

//import com.extjs.gxt.samples.client.examples.model.FileModel;
import pst.arm.client.modules.test.widgets.examples.model.FileModel;
import com.extjs.gxt.ui.client.data.RemoteSortTreeLoadConfig;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async <code>FileService<code> interface.
 */
public interface FileServiceAsync {

  public void getFolderChildren(FileModel model, AsyncCallback<List<FileModel>> children);
  
  public void getFolderChildren(RemoteSortTreeLoadConfig loadConfig, AsyncCallback<List<FileModel>> children);

}
