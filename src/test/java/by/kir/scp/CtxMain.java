package by.kir.scp;

import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
//@ImportResource("classpath:/ctx.xml")
@Import(Routes.class)
public class CtxMain {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/ctx.xml");
        CamelContext context = ctx.getBean(CamelContext.class);
        context.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
