package camel;

import camel.processor.ArrivalProcessor;
import camel.processor.CatchProcessor;
import camel.processor.DepartureProcessor;
import camel.processor.EndOfFishingProcessor;
import camel.processor.LogbookProcessor;
import common.ApplicationVariables;
import domain.Arrival;
import domain.Catch;
import domain.enums.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;
import dto.logbook.LogbookPostDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class ZipParserRouteBuilder extends RouteBuilder {
    private static final String HEADER_NAME = "zipFileName";
    private static final String RESOURCE_URI = "file:c:/datafiles/data_import";

    private Map<String, Map<String, Object>> logbookMap = new HashMap<>();
    private Map<String, List<Catch>> cachesMap = new HashMap<>();

    @Override
    public void configure() throws Exception {
        from(RESOURCE_URI + "?noop=false&delete=true")
                .split(new ZipSplitter())
                .streaming()
                    .choice()
                        .when(header(HEADER_NAME).isEqualTo("Arrival.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Arrival.csv").process(new ArrivalProcessor(logbookMap))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("Departure.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Departure.csv").process(new DepartureProcessor(logbookMap))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("Catch.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Catch.csv").process(new CatchProcessor(cachesMap))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("EndOfFishing.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=EndOfFishing.csv").process(new EndOfFishingProcessor(logbookMap))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("Logbook.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Logbook.csv").process(new LogbookProcessor(logbookMap))
                        .endChoice()
                    .end()
                .end()
                .process(exchange ->{
                    exchange.getOut().setBody(getJsonStringList());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to(ApplicationVariables.HTTP_LOGS_LIST_URI);
    }

    private String getJsonStringList() {
        return createLogbookList()
                .stream()
                .map(LogbookPostDTO::toString)
                .collect(Collectors.toList()).toString();
    }

    private List<LogbookPostDTO> createLogbookList() {
        return logbookMap.entrySet().stream().map(entry ->
                new LogbookPostDTO.Builder()
                        .withArrival((Arrival) entry.getValue().get("arrival"))
                        .withDeparture((Departure) entry.getValue().get("departure"))
                        .withEndOfFishing((EndOfFishing) entry.getValue().get("endOfFishing"))
                        .withCommunicationType(CommunicationType.valueOf(entry.getValue().get("communication").toString()))
                        .withCatches(cachesMap.get(entry.getKey()))
                        .build()
        ).collect(Collectors.toList());
    }
}
