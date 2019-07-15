package camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@Slf4j
public class RuntimeContext {

    private CamelContext context = new DefaultCamelContext();
    @Inject
    private DataSaveRouteBuilder DataSaveRouteBuilder;
    @Inject
    private ZipParserRouterBuilder zipParserRouterBuilder;

    @PostConstruct
    public void init() {
        try {
            context.addRoutes(DataSaveRouteBuilder);
            context.addRoutes(zipParserRouterBuilder);
            context.start();
        } catch (Exception e) {
            log.warn("Error occurred starting CamelContext {} ", context);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            context.stop();
        } catch (Exception e) {
            log.warn("Error occurred stopping CamelContext {} ", context);
        }
    }

}
