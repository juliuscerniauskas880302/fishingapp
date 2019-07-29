package service.logbook;

import domain.Arrival;
import domain.Catch;
import domain.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;
import domain.config.Configuration;
import dto.logbook.LogbookGetDTO;
import dto.logbook.LogbookPostDTO;
import exception.ResourceLockedException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import utilities.PropertyCopierImpl;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;

@RunWith(Arquillian.class)
public class LogbookOptimisticLockTest {
    private static final String LOGBOOK_ID = "2";
    private static final String ARRIVAL_PORT = "ARRIVAL PORT";
    private static final Date ARRIVAL_DATE = new Date();
    private static final String DEPARTURE_PORT = "DEPARTURE PORT";
    private static final Date DEPARTURE_DATE = new Date();
    private static final Date END_OF_FISHING = new Date();
    private static final String VARIETY = "SALMON";
    private static final double WEIGHT = 10.2D;
    private static final CommunicationType COMMUNICATION_TYPE = CommunicationType.NETWORK;
    private static final int VERSION = 3;

    private Arrival arrival;
    private Departure departure;
    private Catch aCatch;
    private EndOfFishing endOfFishing;
    private LogbookPostDTO logbook;

    @Inject
    private LogbookService logbookService;
    @Inject
    private PropertyCopierImpl propertyCopier;

    @Before
    public void init() {
        arrival = new Arrival(ARRIVAL_PORT, ARRIVAL_DATE);
        departure = new Departure(DEPARTURE_PORT, DEPARTURE_DATE);
        aCatch = new Catch(VARIETY, WEIGHT);
        endOfFishing = new EndOfFishing(END_OF_FISHING);

        logbook = new LogbookPostDTO.Builder().withEndOfFishing(endOfFishing)
                .withDeparture(departure)
                .withArrival(arrival).withCommunicationType(COMMUNICATION_TYPE)
                .withCatches(Arrays.asList(aCatch)).build();
    }

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
    public void shouldThrowResourceLockedException() {
        logbookService.save(logbook);

        LogbookGetDTO logbookGetDTO = logbookService.findById(LOGBOOK_ID);

        logbookGetDTO.setCommunicationType(COMMUNICATION_TYPE);
        logbookGetDTO.setArrival(arrival);
        logbookGetDTO.setEndOfFishing(endOfFishing);
        logbookGetDTO.setDeparture(departure);
        logbookGetDTO.setCatches(Arrays.asList(aCatch));

        LogbookPostDTO postDTO = new LogbookPostDTO();
        propertyCopier.copy(postDTO, logbookGetDTO);

        logbookService.update(postDTO, logbookGetDTO.getId());
    }
}


