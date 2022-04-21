/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.client.modules.test.widgets.examples;

import java.util.List;

//import com.extjs.gxt.samples.client.examples.model.BeanPost;
//import com.extjs.gxt.samples.client.examples.model.Photo;
//import com.extjs.gxt.samples.client.examples.model.Post;
//import com.extjs.gxt.samples.resources.client.model.Customer;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExampleServiceAsync {
  //public void getPosts(PagingLoadConfig config, AsyncCallback<PagingLoadResult<Post>> callback);

  //public void getBeanPosts(PagingLoadConfig config, AsyncCallback<PagingLoadResult<BeanPost>> callback);

  //public void getCustomers(AsyncCallback<List<Customer>> callback);

  //public void getPhotos(AsyncCallback<List<Photo>> callback);

  public void getLiveGridModels(PagingLoadConfig config, AsyncCallback<PagingLoadResult<ModelData>> callback);
}
