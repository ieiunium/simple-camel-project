package by.kir.scp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
@Component
public class Routes extends RouteBuilder {

    public void configure() throws Exception {
        from("jetty:http://localhost:8080/")
            .setProperty("1", constant(1))
            .setHeader("hi", constant("hi"))
            .to("direct:soutv")
            .process(exchange -> {
                String body = exchange.getIn().getBody(String.class);
                HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
                exchange.getOut().setBody("<html><body>Book 123 is Camel in Action</body></html>");
            })
            .end();

        from("direct:soutv").id("soutv_id")
            .log("hello world")
            .process(exchange -> System.out.println("exchange = " + exchange))
            .setProperty("1", constant(1))
            .setHeader("hi", constant("hi"))
            .end();
    }
}
