package camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

import javax.ejb.Stateless;

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
                                    .multicast()
                                        .to("file:c:/datafiles/data_import/test")
                                        .end()
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Departure.csv"))
                                    .multicast()
                                        .to("file:c:/datafiles/data_import/test")
                                        .end()
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Catch.csv"))
                                    .multicast()
                                        .to("file:c:/datafiles/data_import/test")
                                        .end()
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("EndOfFishing.csv"))
                                    .multicast()
                                        .to("file:c:/datafiles/data_import/test")
                                        .end()
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Logbook.csv"))
                                    .multicast()
                                        .to("file:c:/datafiles/data_import/test")
                                        .end()
                                .endChoice()

                                .when(header("zipFileName").isEqualTo("Variety.csv"))
                                    .multicast()
                                        .to("file:c:/datafiles/data_import/test")
                                        .end()
                                .endChoice()

                        .end()
                .end();
    }

}
