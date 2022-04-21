package pst.arm.server.modules.payment.dao;

import java.util.List;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface PaymentDAO {
      public List<Payment> getPaymentSumm(PaymentSearchCondition condition);
}
