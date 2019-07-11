package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class RuntimeContext {

    private CamelContext context = new DefaultCamelContext();
    @Inject
    private DataSaveRouteBuilder DataSaveRouteBuilder;

    @PostConstruct
    public void init() {
        try {
            context.addRoutes(DataSaveRouteBuilder);
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
