package camel;

import domain.Arrival;
import domain.Catch;
import domain.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;
import domain.Logbook;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Stateless
@Slf4j
public class ZipParserRouterBuilder extends RouteBuilder {

    private Map<String, Object> logbookMap = new HashMap<>();
    private Map<String, Object> classesMap = new HashMap<>();
    private Map<String, Object> catchesMap = new HashMap<>();

    private static final String HEADER_NAME = "zipFileName";
    private static final String RESOURCE_URI = "file:c:/datafiles/data_import";

    @Override
    public void configure() throws Exception {
        from("file:c:/datafiles/data_import?noop=true&idempotent=true")
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
                .end();
    }

    private void buildLogbook(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "communicationType").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(',').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String communicationType = record.get("communicationType");
                Logbook logbook = new Logbook();
                logbook.setId(id);
                logbook.setCommunicationType(CommunicationType.valueOf(communicationType));
                log.info("Logbook id:{} communication:{}", logbook.getId(), communicationType);
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object. {}", e);
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
                Date date = parseDateFromString(record.get("date"), "yyyy-MM-dd");
                EndOfFishing endOfFishing = new EndOfFishing(date);
                endOfFishing.setId(id);
                log.info("EndOfFishing {}", endOfFishing.toString());
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object.");
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
                log.info("Catch {}", aCatch.toString());
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object.");
        }
    }

    private void buildArrival(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "port", "date").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbook_id = record.get("logbookID");
                String port = record.get("port");
                Date date = parseDateFromString(record.get("date"), "yyyy-MM-dd");
                Arrival arrival = new Arrival(port, date);
                arrival.setId(id);
                log.info("Arrival {}", arrival.toString());
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object.");
        }
    }

    private void buildDeparture(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "port", "date").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbook_id = record.get("logbookID");
                String port = record.get("port");
                Date date = parseDateFromString(record.get("date"), "yyyy-MM-dd");
                Departure departure = new Departure(port, date);
                departure.setId(id);
                log.info("Departure {}", departure.toString());
            }
        } catch (IOException e) {
            log.error("Error occurred building arrival object.");
        }
    }

    public Date parseDateFromString(String dateString, String datePattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error("Cannot parse string {} to date.", dateString);
        }
        return date;
    }

}
