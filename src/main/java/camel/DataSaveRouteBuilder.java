package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Logbook;
import enums.CommunicationType;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.io.File;

public class DataSaveRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:C:\\datafiles\\input\\?noop=false&delete=true")
                .process(exchange -> {
                    File file = exchange.getIn().getBody(File.class);
                    ObjectMapper mapper = new ObjectMapper();
                    Logbook logbook;
                    logbook = mapper.readValue(file, Logbook.class);
                    logbook.setCommunicationType(CommunicationType.SATELLITE);
                    exchange.getOut().setBody(logbook.toString());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:8080/fishingapp/api/logs/");
    }
}
