package camel.processor;

import common.ApplicationVariables;
import domain.Departure;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.DateUtilities;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DepartureProcessor implements Processor {
    private static final Logger LOG = LogManager.getLogger(DepartureProcessor.class);

    private Map<String, Map<String, Object>> logbookMap;

    public DepartureProcessor(Map<String, Map<String, Object>> logbookMap) {
        this.logbookMap = logbookMap;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String filePath = exchange.getIn().getBody(File.class).getAbsolutePath();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("ID", "logbookID", "port", "date").withIgnoreHeaderCase().withSkipHeaderRecord()
                     .withDelimiter(';').withTrim())) {
            for (CSVRecord record : parser) {
                String id = record.get("ID");
                String logbookId = record.get("logbookID");
                String port = record.get("port");
                Date date = DateUtilities.parseDateFromString(record.get("date"), ApplicationVariables.DATE_PATTERN);
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
            LOG.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }
}
