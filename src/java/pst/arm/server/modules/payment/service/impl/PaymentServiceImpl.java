package pst.arm.server.modules.payment.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;
import pst.arm.server.modules.payment.dao.PaymentDAO;
import pst.arm.server.modules.payment.service.PaymentService;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentDAO dao;
    private static Logger log = Logger.getLogger("PaymentServiceImpl");

    @Autowired
    public void setDao(PaymentDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Payment> getPayment(PaymentSearchCondition condition) {
        return dao.getPaymentSumm(condition);
    }
}
