package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class RuntimeContext {
    CamelContext context = new DefaultCamelContext();

    @PostConstruct
    public void init() {
        try {
            context.addRoutes(new DataSaveRouteBuilder());
            context.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            context.stop();
        } catch (Exception e) {
        }
    }
}
