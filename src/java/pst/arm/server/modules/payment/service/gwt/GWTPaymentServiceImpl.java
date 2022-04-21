package pst.arm.server.modules.payment.service.gwt;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.common.exception.RpcServiceException;
import pst.arm.client.modules.payment.domain.Payment;
import pst.arm.client.modules.payment.domain.search.PaymentSearchCondition;
import pst.arm.client.modules.payment.service.remote.GWTPaymentService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.payment.service.PaymentService;


/**
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service("GWTPaymentService")
public class GWTPaymentServiceImpl extends GWTController implements GWTPaymentService {

    private PaymentService service;
    private static Logger log = Logger.getLogger("GWTPaymentServiceImpl");

    @Autowired
    public void setPaymentService(PaymentService service) {
        this.service = service;
    }

    @Override
    public List<Payment> getPayment(PaymentSearchCondition condition) throws RpcServiceException {
        try {
            return service.getPayment(condition);
        } catch (Exception ex) {
            throw new RpcServiceException("Ошибка при получении списка", ex.getMessage());
        }
    }
    @Override
    public Integer test() throws RpcServiceException {
        {
            try {
                return 100;
            } catch (Exception ex) {
                throw new RpcServiceException("Ошибка при получении списка", ex.getMessage());
            }
        }
    }
}