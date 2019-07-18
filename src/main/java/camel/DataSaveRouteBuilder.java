package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.ApplicationVariables;
import domain.Logbook;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import javax.ejb.Stateless;
import java.io.File;

@Stateless
public class DataSaveRouteBuilder extends RouteBuilder {

    private static final String FILE_URI = "file:C:/datafiles/inbox/";

    @Override
    public void configure() throws Exception {
        from("timer://schedulerTimer?fixedRate=true&period=5s&delay=5s")
                .pollEnrich(FILE_URI + "?noop=false&delete=true")
                .process(exchange -> {
                    File file = exchange.getIn().getBody(File.class);
                    ObjectMapper mapper = new ObjectMapper();
                    Logbook logbook;
                    logbook = mapper.readValue(file, Logbook.class);
                    exchange.getOut().setBody(logbook.toString());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to(ApplicationVariables.HTTP_LOGS_URI);
    }

}
