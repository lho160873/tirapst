package pst.arm.server.common.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;


/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class StaticContextHolder implements BeanFactoryAware {

    public static BeanFactory CONTEXT;

    public StaticContextHolder() {
    }

    public static Object getBean(String s) throws BeansException {
        return CONTEXT.getBean(s);
    }

    public static <T> T getBean(String s, Class<T> tClass) throws BeansException {
        return CONTEXT.getBean(s, tClass);
    }

    public static <T> T getBean(Class<T> tClass) throws BeansException {
        return CONTEXT.getBean(tClass);
    }

    public static Object getBean(String s, Object... objects) throws BeansException {
        return CONTEXT.getBean(s, objects);
    }

    public static boolean containsBean(String s) {
        return CONTEXT.containsBean(s);
    }


    @Override
    public void setBeanFactory(BeanFactory applicationContext) throws BeansException {
        //logger.assertNull(CONTEXT, "CONTEXT is not null. Double Spring context creation?");
        CONTEXT = applicationContext;
    }

}

