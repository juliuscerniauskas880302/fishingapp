package camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

import javax.ejb.Stateless;
import java.io.File;

@Stateless
public class ZipParserRouterBuilder extends RouteBuilder {

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
                                                System.out.println("FILE PATH = "+ file);
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

}
