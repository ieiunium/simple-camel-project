package by.kir.scp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
public class MyBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RouteBuilder) {
            RouteBuilder builder = (RouteBuilder) bean;
            builder.setRouteCollection(wrapRoutesDefinition(builder.getRouteCollection()));
            return bean;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RouteBuilder) {
        }
        return bean;
    }

    private RoutesDefinition wrapRoutesDefinition(final RoutesDefinition object) {
        Enhancer enhancer = getEnhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//                System.out.println(object);
                Object invoke = method.invoke(object, args);
                if (method.getReturnType().isAssignableFrom(RouteDefinition.class)) {
                    System.out.println(method);
                    return wrapProcessorDefinition((RouteDefinition) invoke);
                }
                return invoke;
            }
        });
        return (RoutesDefinition) enhancer.create();
    }

    private ProcessorDefinition wrapProcessorDefinition(final ProcessorDefinition object) {
        Enhancer enhancer = getEnhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                Object invoke = method.invoke(object, args);
                System.out.println("method = " + method);
                System.out.println("object = " + object);
                System.out.println("invoke = " + invoke);
                List<StackTraceElement> collect = getStackTraceElements();
                collect
                    .forEach(stackTraceElement -> System.out.println("\t" + stackTraceElement));
                if (collect.size() > 0) {
                    StackTraceElement stackTraceElement = collect.get(0);
                    if (object.getOutputs() != null && object.getOutputs().size() != 0) {
                        int indexOfLast = object.getOutputs().size() - 1;
                        ProcessorDefinition
                            lastOutput = (ProcessorDefinition) object.getOutputs().get(indexOfLast);
                        lastOutput.addInterceptStrategy(new MyInterceptStrategy(stackTraceElement));
                    }
                }
                if (ProcessorDefinition.class.isAssignableFrom(method.getReturnType())) {
                    if (object == invoke) {
                        return o;
                    } else {
                        return wrapProcessorDefinition((ProcessorDefinition) invoke);
                    }
                }
                return invoke;
            }
        });
        return (ProcessorDefinition) enhancer.create();
    }

    private List<StackTraceElement> getStackTraceElements() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return Arrays
            .stream(stackTrace)
            .filter(stackTraceElement -> !stackTraceElement.getClassName().contains("org.apache"))
            .filter(stackTraceElement -> !stackTraceElement.getClassName().contains("org.spring"))
            .filter(stackTraceElement -> !stackTraceElement.getClassName().contains("java.lang"))
//                        .filter(stackTraceElement -> !stackTraceElement.getClassName().contains("RouteDefinition"))
            .filter(stackTraceElement -> !stackTraceElement.getClassName().contains("MyBeanPostProcessor"))
            .collect(Collectors.toList());
    }

    private Enhancer getEnhancer()  {

//        try {
//            String
//                url =
//                "file:/home/kiryl_chepeleu/.m2/repository/org/springframework/spring-core/3.2.12.RELEASE/spring-core-3.2.12.RELEASE.jar";
//            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(
//                url)}, ClassLoader.getSystemClassLoader());
//            Class<Enhancer>
//                enhancerClass = (Class<Enhancer>) Class.forName(Enhancer.class.getName(), true, urlClassLoader);
//            return enhancerClass.newInstance();
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | MalformedURLException e) {
//            e.printStackTrace();
//        }
        return new Enhancer();
    }
}
