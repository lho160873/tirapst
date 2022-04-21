package pst.arm.client.modules.payment.service.remote;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@RemoteServiceRelativePath("service/paymentService")
public interface GWTPaymentService extends RemoteService {

    public List<Payment> getPayment(PaymentSearchCondition condition) throws RpcServiceException;

   public Integer test() throws RpcServiceException;
    
}