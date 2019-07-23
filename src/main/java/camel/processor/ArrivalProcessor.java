package camel.processor;

import common.ApplicationVariables;
import domain.Arrival;
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

public class ArrivalProcessor implements Processor {
    private static final String ID = "ID";
    private static final String LOGBOOK_ID = "logbookID";
    private static final String PORT = "port";
    private static final String DATE = "date";
    private static final char DELIMITER = ';';
    private static final Logger LOG = LogManager.getLogger(ArrivalProcessor.class);

    private Map<String, Map<String, Object>> logbookMap;

    public ArrivalProcessor(Map<String, Map<String, Object>> logbookMap) {
        this.logbookMap = logbookMap;
    }

    @Override
    public void process(Exchange exchange) {
        String filePath = exchange.getIn().getBody(File.class).getAbsolutePath();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = getParser(reader)) {
            parser.forEach(this::mapObject);
        } catch (IOException e) {
            LOG.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }

    private void mapObject(CSVRecord record) {
        String id = record.get(ID);
        String logbookId = record.get(LOGBOOK_ID);
        String port = record.get(PORT);
        Date date = DateUtilities.parseDateFromString(record.get(DATE), ApplicationVariables.DATE_PATTERN);
        Arrival arrival = new Arrival(port, date);
        arrival.setId(id);
        logbookMap.putIfAbsent(logbookId, new HashMap<>());
        logbookMap.get(logbookId).put("arrival", arrival);
    }

    private CSVParser getParser(Reader reader) throws IOException {
        return new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader(ID, LOGBOOK_ID, PORT, DATE).withIgnoreHeaderCase().withSkipHeaderRecord()
                .withDelimiter(DELIMITER).withTrim());
    }
}
