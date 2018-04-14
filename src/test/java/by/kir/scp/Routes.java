package by.kir.scp;

import static by.kir.scp.MyInterceptStrategy.MY_STACK_TRACE_HISTORY;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.MessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
@Component
public class Routes extends RouteBuilder {

    public void configure() throws Exception {
        onCompletion()
            .process(exchange -> System.out.println(MessageHelper.dumpMessageHistoryStacktrace(exchange, null, true)))
            .process(exchange -> ((List)exchange.getProperty(MY_STACK_TRACE_HISTORY))
                .stream()
                .forEach(System.out::println)
            );

        from("jetty:http://localhost:8080/")
            .process(exchange -> {
                exchange.getOut().setBody("<html><body>Book 123 is Camel in Action</body></html>");
            })
            .setProperty("1", constant(1))
            .setProperty( "-1" )
            .xquery( "local-name(//*[ends-with(local-name(), 'body')])", String.class )
            .setHeader("hi", constant("hi"))
            .filter(Exchange::hasProperties)
                .setProperty("11", constant(11))
                .setHeader("11", constant("11"))
                .filter(Exchange::hasProperties)
                    .setHeader("33", constant("33"))
                    .filter(Exchange::hasProperties)
                        .setHeader("44", constant("44"))
                        .setProperty("44", constant("44"))
                    .end()
                    .setProperty("33", constant("33"))
                .end()
            .end()
            .to("direct:soutv")

            .end();

        from("direct:soutv").id("soutv_id")
            .log("hello world")
            .process(exchange -> System.out.println("exchange = " + exchange))
            .setProperty("2", constant(2))
            .setHeader("hi1", constant("hi1"))
            .end();
    }
}
