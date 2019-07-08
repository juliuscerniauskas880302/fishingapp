package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Logbook;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.io.File;

public class DataSaveRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer://schedulerTimer?fixedRate=true&period=5s&delay=5s")
                .pollEnrich("file:C:\\datafiles\\inbox\\?noop=false&delete=true")
                .process(exchange -> {
                    File file = exchange.getIn().getBody(File.class);
                    ObjectMapper mapper = new ObjectMapper();
                    Logbook logbook;
                    logbook = mapper.readValue(file, Logbook.class);
                    exchange.getOut().setBody(logbook.toString());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:8080/fishingapp/api/logs/");
    }
}
