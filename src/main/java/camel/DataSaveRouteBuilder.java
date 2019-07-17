package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Logbook;
import org.apache.camel.builder.RouteBuilder;
import service.logbook.LogbookService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;

@Stateless
public class DataSaveRouteBuilder extends RouteBuilder {
    @Inject
    private LogbookService logbookService;

    @Override
    public void configure() throws Exception {
        from("timer://schedulerTimer?fixedRate=true&period=5s&delay=5s")
                .pollEnrich("file:C:\\datafiles\\inbox\\?noop=false&delete=true")
                .process(exchange -> {
                    File file = exchange.getIn().getBody(File.class);
                    ObjectMapper mapper = new ObjectMapper();
                    Logbook logbook;
                    logbook = mapper.readValue(file, Logbook.class);
                    exchange.getOut().setBody(logbook);
                })
                .bean(logbookService,"save");
    }

}
