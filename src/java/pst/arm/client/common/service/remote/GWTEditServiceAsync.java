package pst.arm.client.common.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
//import pst.arm.client.common.domain.search.SearchCondition;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.14.0
 */
public interface GWTEditServiceAsync<T> {
    public void save(T domain,  Boolean isNew,AsyncCallback<T> callback);
    public void getDomainById(Long id, AsyncCallback<T> callback);
    public void deleteDomainsByIds(List<Integer> ids, AsyncCallback<Boolean> callback );
}
