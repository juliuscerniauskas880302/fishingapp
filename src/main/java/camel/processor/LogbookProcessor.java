package camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LogbookProcessor implements Processor {
    private static final Logger LOG = LogManager.getLogger(LogbookProcessor.class);
    private static final String ID = "ID";
    private static final String COMMUNICATION = "communication";
    private static final String COMMUNICATION_TYPE = "communicationType";
    private static final char DELIMITER = ',';

    private Map<String, Map<String, Object>> logbookMap;

    public LogbookProcessor(Map<String, Map<String, Object>> logbookMap) {
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
        String communicationType = record.get(COMMUNICATION_TYPE);
        logbookMap.putIfAbsent(id, new HashMap<>());
        logbookMap.get(id).put(COMMUNICATION, communicationType);
    }

    private CSVParser getParser(Reader reader) throws IOException {
        return new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader(ID, COMMUNICATION_TYPE).withIgnoreHeaderCase().withSkipHeaderRecord()
                .withDelimiter(DELIMITER).withTrim());
    }
}
