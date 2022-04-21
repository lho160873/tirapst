package pst.arm.client.common.service.remote;

import java.util.List;
import pst.arm.client.common.exception.RpcServiceException;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.14.0
 */
public interface GWTEditService<T> {
    public T save(T domain, Boolean isNew) throws RpcServiceException;
    public T getDomainById(Long id) throws RpcServiceException;
    public Boolean deleteDomainsByIds(List<Integer> ids) throws RpcServiceException;
}
