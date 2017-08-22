package by.kir.scp.my;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;

/**
 * Created by kiryl_chepeleu on 8/22/17.
 */
public class MyRoutesDefenition extends RoutesDefinition{

    @Override
    protected RouteDefinition createRoute() {
        RouteDefinition superRoute = super.createRoute();
        RouteDefinition route = new MyRouteDefinition();
        route.setErrorHandlerBuilderIfNull(superRoute.getErrorHandlerBuilder());
        return route;
    }
}
