package service.logbook;

import domain.Arrival;
import domain.Catch;
import domain.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;
import domain.Logbook;
import domain.config.Configuration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.exception.ResourceLockedException;

import javax.inject.Inject;
import java.util.Arrays;

@RunWith(Arquillian.class)
public class LogbookOptimisticLockTest {
    private static final String ID = "2";

    @Inject
    private LogbookService logbookService;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "org.apache.logging.log4j", "domain", "strategy", "service")
                .addPackage(Configuration.class.getPackage())
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("log4j2.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test(expected = ResourceLockedException.class)
    public void shouldThrowResourceLockedException() throws Exception {
        Logbook logbook = new Logbook.Builder().withEndOfFishing(new EndOfFishing())
                .withDeparture(new Departure()).withArrival(new Arrival()).withCommunicationType(CommunicationType.NETWORK)
                .withCatches(Arrays.asList(new Catch())).withId(ID).build();
        logbookService.save(logbook);
        Logbook log1 = logbookService.findById(ID);
        logbookService.update(log1, ID);
    }
}


