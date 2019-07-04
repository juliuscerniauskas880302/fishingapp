package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Logbook;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.io.File;

public class DataSaveRouterBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:C:\\datafiles\\input")
                .setHeader(Exchange.FILE_NAME, constant("logbook.log"))
                .process(exchange -> {
                    File file = exchange.getIn().getBody(File.class);
                    ObjectMapper mapper = new ObjectMapper();
                    Logbook logbook = null;
                    logbook = mapper.readValue(file, Logbook.class);
                    exchange.getOut().setBody(logbook);
                })
        .to("activemq:queue:fish_queue");
    }
}