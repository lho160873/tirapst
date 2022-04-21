package pst.arm.client.modules.payment.service.remote;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;


/**
 *
 * @author LKHorosheva
 * @since 0.0.2
 */
public interface GWTPaymentServiceAsync {
     
        public void getPayment(PaymentSearchCondition condition, AsyncCallback<List<Payment>> callback);
        public void test(AsyncCallback<Integer> callback) ;
}
