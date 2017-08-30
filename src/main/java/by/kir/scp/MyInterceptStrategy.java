package by.kir.scp;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.InterceptStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kiryl_chepeleu on 8/29/17.
 */
public class MyInterceptStrategy implements InterceptStrategy {

    public static final String MY_STACK_TRACE_HISTORY = "myStackTraceHistory";
    private final StackTraceElement stackTraceElement;

    public MyInterceptStrategy(StackTraceElement stackTraceElement) {
        this.stackTraceElement = stackTraceElement;
    }

    @Override
    public Processor wrapProcessorInInterceptors(CamelContext context, ProcessorDefinition<?> definition,
                                                 Processor target, Processor nextTarget) throws Exception {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
//                                        LOGGER.info("definition.getIndex() = " + definition.getIndex());
//                                        LOGGER.info("stackTraceElement = " + stackTraceElement);
                definition
                    .getInterceptStrategies()
                    .stream()
                    .filter(interceptStrategy -> interceptStrategy instanceof MyInterceptStrategy)
                    .findFirst()
                    .ifPresent(interceptStrategy -> {
                        StackTraceElement
                            traceElement =
                            ((MyInterceptStrategy) interceptStrategy).stackTraceElement;
                        System.out.println("stackTraceElement = " + traceElement);
                        List<StackTraceElement>
                            myStackTraceHistory = (List<StackTraceElement>) exchange.getProperty(MY_STACK_TRACE_HISTORY);
                        if(myStackTraceHistory == null){
                            exchange.setProperty(MY_STACK_TRACE_HISTORY, new ArrayList(
                                Collections.singletonList(traceElement)));
                        }else {
                            myStackTraceHistory.add(traceElement);
                        }
                });

//                                        System.out.println("target = " + target);
//                                        System.out.println("nextTarget = " + nextTarget);
                System.out.println("definition = " + definition);
//                                        System.out.println();
                target.process(exchange);
            }
        };
    }

}
