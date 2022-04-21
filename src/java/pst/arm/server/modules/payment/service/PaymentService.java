package pst.arm.server.modules.payment.service;

import java.util.List;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface PaymentService {
    public List<Payment> getPayment(PaymentSearchCondition condition);
}
