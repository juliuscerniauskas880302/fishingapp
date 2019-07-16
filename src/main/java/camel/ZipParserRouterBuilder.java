package camel;

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
import java.util.HashMap;
import java.util.Map;

@Stateless
public class ZipParserRouterBuilder extends RouteBuilder {

    private Map<String, Object> logbookMap = new HashMap<>();
    private Map<String, Object> claseesMap = new HashMap<>();
    private Map<String, Object> catchesMap = new HashMap<>();

    @Override
    public void configure() throws Exception {
        from("file:c:/datafiles/data_import?noop=true&idempotent=true")
                .streamCaching()
                    .split(new ZipSplitter())
                        .streaming()
                            .choice()
                                .when(header("zipFileName").isEqualTo("Arrival.csv"))
                                        .to("file:c:/datafiles/data_import/arrival")
                                        .pollEnrich("file:c:/datafiles/data_import/arrival").process().exchange(exchange ->
                                            {
                                                File file = exchange.getIn().getBody(File.class);
                                                buildArrival(file.getAbsolutePath());

                                            })

                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Departure.csv"))
                                        .to("file:c:/datafiles/data_import/departure")
                                        .pollEnrich("file:c:/datafiles/data_import/departure").process().exchange(exchange ->
                                            {
                                                File file = exchange.getIn().getBody(File.class);
                                                System.out.println("FILE PATH = "+ file);
                                            })
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Catch.csv"))
                                        .to("file:c:/datafiles/data_import/catch")
                                        .pollEnrich("file:c:/datafiles/data_import/catch").process().exchange(exchange ->
                                            {
                                                File file = exchange.getIn().getBody(File.class);
                                                System.out.println("FILE PATH = "+ file);
                                            })
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("EndOfFishing.csv"))
                                        .to("file:c:/datafiles/data_import/endOfFishing")
                                        .pollEnrich("file:c:/datafiles/data_import/endOfFishing").process().exchange(exchange ->
                                            {
                                                File file = exchange.getIn().getBody(File.class);
                                                System.out.println("FILE PATH = "+ file);
                                            })
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Logbook.csv"))
                                        .to("file:c:/datafiles/data_import/logbook")
                                        .pollEnrich("file:c:/datafiles/data_import/logbook").process().exchange(exchange ->
                                            {
                                                File file = exchange.getIn().getBody(File.class);
                                                System.out.println("FILE PATH = "+ file);
                                            })
                                .endChoice()

                        .end()
                .end();
    }

    private void buildArrival(String filePath) {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim())) {
            for(CSVRecord record: parser){
                String id = record.get("id");
                String logbook_id = record.get("logbook_id");
                String port = record.get("port_id");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
