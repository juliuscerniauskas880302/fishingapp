package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Logbook;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

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
                .marshal().json(JsonLibrary.Gson)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:8080/deployments/logs");
    }
}