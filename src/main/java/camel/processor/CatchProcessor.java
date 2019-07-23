package camel.processor;

import domain.Catch;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatchProcessor implements Processor {
    private static final Logger LOG = LogManager.getLogger(EndOfFishingProcessor.class);
    private static final String ID = "ID";
    private static final String LOGBOOK_ID = "logbookID";
    private static final String VARIETY = "variety";
    private static final String WEIGHT = "weight";
    private static final char DELIMITER = ';';

    private Map<String, List<Catch>> cachesMap;

    public CatchProcessor(Map<String, List<Catch>> cachesMap) {
        this.cachesMap = cachesMap;
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
        String variety = record.get(VARIETY);
        Double weight = Double.parseDouble(record.get(WEIGHT));
        Catch aCatch = new Catch(variety, weight);
        aCatch.setId(id);
        cachesMap.putIfAbsent(logbookId, new ArrayList<>());
        cachesMap.get(logbookId).add(aCatch);
    }
    private CSVParser getParser(Reader reader) throws IOException {
        return new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader(ID, LOGBOOK_ID, VARIETY, WEIGHT).withIgnoreHeaderCase().withSkipHeaderRecord()
                .withDelimiter(DELIMITER).withTrim());
    }
}
