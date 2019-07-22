package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class RuntimeContext {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeContext.class);

    private CamelContext context = new DefaultCamelContext();

    @Inject
    private DataSaveRouteBuilder DataSaveRouteBuilder;
    @Inject
    private ZipParserRouteBuilder zipParserRouteBuilder;

    @PostConstruct
    public void init() {
        try {
            context.addRoutes(DataSaveRouteBuilder);
            context.addRoutes(zipParserRouteBuilder);
            context.start();
        } catch (Exception e) {
            LOG.error("Error occurred starting CamelContext {} ", context);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            context.stop();
        } catch (Exception e) {
            LOG.error("Error occurred stopping CamelContext {} ", context);
        }
    }
}
