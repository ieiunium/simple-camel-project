package by.kir.scp;

import by.kir.scp.my.MyRoutesDefenition;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RouteBuilder) {
            RouteBuilder builder = (RouteBuilder) bean;
            builder.setRouteCollection(new MyRoutesDefenition());
            System.out.println();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RouteBuilder) {
            System.out.println();
        }
        return bean;
    }
}
