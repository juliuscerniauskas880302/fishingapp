package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class DataSaveRouteContext {
    public static void save() {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new DataSaveRouterBuilder());
            context.start();
            Thread.sleep(6000);
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
