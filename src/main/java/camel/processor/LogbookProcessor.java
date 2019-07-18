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

    private Map<String, Map<String, Object>> logbookMap;

    public LogbookProcessor(Map<String, Map<String, Object>> logbookMap) {
        this.logbookMap = logbookMap;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String filePath = exchange.getIn().getBody(File.class).getAbsolutePath();
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
            LOG.error("Error occurred building arrival object. {}", e.getMessage());
        }
    }
}
