package by.kir.scp.my;

import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
public class MyRouteDefinition extends RouteDefinition {

    @Override
    public void addOutput(ProcessorDefinition<?> output) {
        System.out.println(output.toString());
        Thread.dumpStack();
        super.addOutput(output);
    }
}
