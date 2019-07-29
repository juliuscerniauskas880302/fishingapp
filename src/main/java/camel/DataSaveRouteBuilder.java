package camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.ApplicationVariables;
import domain.Logbook;
import dto.logbook.LogbookPostDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import utilities.PropertyCopierImpl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;

@Stateless
public class DataSaveRouteBuilder extends RouteBuilder {
    private static final String FILE_URI = "file:C:/datafiles/inbox/";
    public static final String TIMER = "timer://schedulerTimer?fixedRate=true&period=5s&delay=5s";

    @Inject
    private PropertyCopierImpl propertyCopier;

    @Override
    public void configure() throws Exception {
        from(TIMER)
                .pollEnrich(FILE_URI + "?noop=false&delete=true")
                .process(exchange -> {
                    File file = exchange.getIn().getBody(File.class);
                    ObjectMapper mapper = new ObjectMapper();
                    Logbook logbook;
                    logbook = mapper.readValue(file, Logbook.class);
                    LogbookPostDTO dto = new LogbookPostDTO();
                    propertyCopier.copy(dto, logbook);
                    exchange.getOut().setBody(dto.toString());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to(ApplicationVariables.HTTP_LOGS_URI);
    }
}
