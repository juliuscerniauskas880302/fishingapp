package camel;

import domain.Arrival;
import domain.Catch;
import domain.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;
import domain.Logbook;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import utilities.DateUtilities;

import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
@Slf4j
public class ZipParserRouterBuilder extends RouteBuilder {

    private Map<String, Map<String, Object>> logbookMap = new HashMap<>();
    private Map<String, List<Catch>> cachesMap = new HashMap<>();
    private static final String HEADER_NAME = "zipFileName";
    private static final String RESOURCE_URI = "file:c:/datafiles/data_import";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void configure() throws Exception {
        from(RESOURCE_URI + "?noop=true&idempotent=true")
                .split(new ZipSplitter())
                .streaming()
                    .choice()
                        .when(header(HEADER_NAME).isEqualTo("Arrival.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Arrival.csv").process().exchange(exchange ->
                                buildArrival(exchange.getIn().getBody(File.class).getAbsolutePath()))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("Departure.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Departure.csv").process().exchange(exchange ->
                                buildDeparture(exchange.getIn().getBody(File.class).getAbsolutePath()))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("Catch.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Catch.csv").process().exchange(exchange ->
                                buildCatch(exchange.getIn().getBody(File.class).getAbsolutePath()))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("EndOfFishing.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=EndOfFishing.csv").process().exchange(exchange ->
                                buildEndOfFishing(exchange.getIn().getBody(File.class).getAbsolutePath()))
                        .endChoice()
                        .when(header(HEADER_NAME).isEqualTo("Logbook.csv"))
                            .to(RESOURCE_URI)
                            .pollEnrich(RESOURCE_URI + "?fileName=Logbook.csv").process().exchange(exchange ->
                                buildLogbook(exchange.getIn().getBody(File.class).getAbsolutePath()))
                        .endChoice()
                .end().end().process(exchange -> {
            List<String> logbookJsonList = createLogbookList().stream().map(Logbook::toString).collect(Collectors.toList());
            exchange.getOut().setBody(logbookJsonList.toString());
        })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:8080/fishingapp/api/logs/logs/");
    }

    private void buildLogbook(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "communicationType").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(',').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String communicationType = record.get("communicationType");
                if (!logbookMap.containsKey(id)) {
                    logbookMap.put(id, new HashMap<>());
                    logbookMap.get(id).put("communication", communicationType);
                } else {
                    logbookMap.get(id).put("communication", communicationType);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }

    private void buildEndOfFishing(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "date").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbookId = record.get("logbookID");
                Date date = DateUtilities.parseDateFromString(record.get("date"), DATE_PATTERN);
                EndOfFishing endOfFishing = new EndOfFishing(date);
                endOfFishing.setId(id);
                if (!logbookMap.containsKey(logbookId)) {
                    logbookMap.put(logbookId, new HashMap<>());
                    logbookMap.get(logbookId).put("endOfFishing", endOfFishing);
                } else {
                    logbookMap.get(logbookId).put("endOfFishing", endOfFishing);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }

    private void buildCatch(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "variety", "weight").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbookId = record.get("logbookID");
                String variety = record.get("variety");
                Double weight = Double.parseDouble(record.get("weight"));
                Catch aCatch = new Catch(variety, weight);
                aCatch.setId(id);
                if (!cachesMap.containsKey(logbookId)) {
                    cachesMap.put(logbookId, new ArrayList<>());
                    cachesMap.get(logbookId).add(aCatch);
                } else {
                    cachesMap.get(logbookId).add(aCatch);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }

    private void buildArrival(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "port", "date").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbookId = record.get("logbookID");
                String port = record.get("port");
                Date date = DateUtilities.parseDateFromString(record.get("date"), DATE_PATTERN);
                Arrival arrival = new Arrival(port, date);
                arrival.setId(id);
                if (!logbookMap.containsKey(logbookId)) {
                    logbookMap.put(logbookId, new HashMap<>());
                    logbookMap.get(logbookId).put("arrival", arrival);
                } else {
                    logbookMap.get(logbookId).put("arrival", arrival);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }

    private void buildDeparture(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "port", "date").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbookId = record.get("logbookID");
                String port = record.get("port");
                Date date = DateUtilities.parseDateFromString(record.get("date"), DATE_PATTERN);
                Departure departure = new Departure(port, date);
                departure.setId(id);
                if (!logbookMap.containsKey(logbookId)) {
                    logbookMap.put(logbookId, new HashMap<>());
                    logbookMap.get(logbookId).put("departure", departure);
                } else {
                    logbookMap.get(logbookId).put("departure", departure);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }

    private List<Logbook> createLogbookList() {
        return logbookMap.entrySet().stream().map(entry ->
                new Logbook.Builder()
                        .withId(entry.getKey()).withArrival((Arrival) entry.getValue().get("arrival"))
                        .withDeparture((Departure) entry.getValue().get("departure"))
                        .withEndOfFishing((EndOfFishing) entry.getValue().get("endOfFishing"))
                        .withCommunicationType(CommunicationType.valueOf(entry.getValue().get("communication").toString()))
                        .withCatches(cachesMap.get(entry.getKey()))
                        .build()
        ).collect(Collectors.toList());
    }

}
