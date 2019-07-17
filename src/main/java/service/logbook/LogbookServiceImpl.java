package service.logbook;

import common.ApplicationVariables;
import domain.CommunicationType;
import domain.Logbook;
import io.xlate.inject.Property;
import io.xlate.inject.PropertyResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strategy.DatabaseSavingStrategy;
import strategy.FileSavingStrategy;
import strategy.SavingStrategy;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
@SuppressWarnings("unchecked")
public class LogbookServiceImpl implements LogbookService {
    private static final Logger LOG = LogManager.getLogger(LogbookServiceImpl.class);
    private static final String FIND_ALL_LOGBOOKS_BY_SPECIES = "SELECT DISTINCT (LOGBOOK.*)" +
            " FROM LOGBOOK " +
            " LEFT JOIN LOGBOOK_CATCH LC ON LOGBOOK.ID = LC.LOGBOOK_ID" +
            " LEFT JOIN CATCH C ON LC.CATCHES_ID = C.ID" +
            " WHERE C.VARIETY LIKE ?1 group by LOGBOOK.ID";
    private static final String GET_ALL_LOGBOOKS = "SELECT * FROM LOGBOOK";
    private static final String FIND_ALL_LOGBOOKS_BY_PORT = "SELECT * FROM LOGBOOK" +
            " LEFT JOIN ARRIVAL A ON LOGBOOK.ARRIVAL_ID = A.ID" +
            " LEFT JOIN DEPARTURE D ON LOGBOOK.DEPARTURE_ID = D.ID" +
            " WHERE A.PORT LIKE ?1 OR D.PORT LIKE ?1";
    private static final String FIND_BY_DEPARTURE_DATE = "SELECT LOGBOOK.* FROM LOGBOOK" +
            " INNER JOIN DEPARTURE A on LOGBOOK.DEPARTURE_ID = A.ID" +
            " WHERE DATE BETWEEN ?1 AND ?2";
    private static final String FIND_BY_ARRIVAL_DATE = "SELECT LOGBOOK.* FROM LOGBOOK" +
            " INNER JOIN ARRIVAL A on LOGBOOK.ARRIVAL_ID = A.ID" +
            " WHERE DATE BETWEEN ?1 AND ?2";

    @Inject
    @Property(name = "strategy.file.filePath",
            resource = @PropertyResource(ApplicationVariables.PROPERTIES_FILE_PATH),
            defaultValue = "C:/datafiles/satellite/notfound")
    private String FILE_PATH;

    @PersistenceContext
    private EntityManager manager;

    private SavingStrategy savingStrategy;

    @Override
    public Logbook findById(String id) {
        return Optional.ofNullable(manager.find(Logbook.class, id)).orElse(null);
    }

    @Override
    public List<Logbook> findAll() {
        return Optional.ofNullable(manager.createNativeQuery(GET_ALL_LOGBOOKS, Logbook.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Logbook source) {
        if (CommunicationType.SATELLITE.equals(source.getCommunicationType())) {
            savingStrategy = new FileSavingStrategy(FILE_PATH);
        } else {
            savingStrategy = new DatabaseSavingStrategy(manager);
        }
        LOG.info("Logbook {} has been created using {} strategy.", source.toString(), savingStrategy.getClass().getName());
        savingStrategy.save(source);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Logbook source, String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook -> {
            logbook.setCommunicationType(source.getCommunicationType());
            logbook.setArrival(source.getArrival());
            logbook.setCatches(source.getCatches());
            logbook.setDeparture(source.getDeparture());
            logbook.setEndOfFishing(source.getEndOfFishing());
            manager.merge(logbook);
            LOG.info("Logbook '{}' has been updated.", id);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook ->
                manager.remove(logbook));
        LOG.info("Logbook '{}' has been deleted.", id);
    }

    @Override
    public List<Logbook> findByPort(String port) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_LOGBOOKS_BY_PORT, Logbook.class)
                .setParameter(1, port)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findBySpecies(String species) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_LOGBOOKS_BY_SPECIES, Logbook.class)
                .setParameter(1, species)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findByArrivalDateIn(String date1, String date2) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_BY_ARRIVAL_DATE, Logbook.class)
                .setParameter(1, date1)
                .setParameter(2, date2)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findByDepartureDateIn(String date1, String date2) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_BY_DEPARTURE_DATE, Logbook.class)
                .setParameter(1, date1)
                .setParameter(2, date2)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public Response saveAll(List<Logbook> logbooks) {
        logbooks.forEach(logbook -> {
            if (CommunicationType.SATELLITE.equals(logbook.getCommunicationType())) {
                savingStrategy = new FileSavingStrategy(FILE_PATH);
            } else {
                savingStrategy = new DatabaseSavingStrategy(manager);
            }
            LOG.info("Logbook {} has been created using {} strategy.", logbook.getId(), savingStrategy.getClass().getName());
            savingStrategy.save(logbook);
        });
        return Response.status(Response.Status.OK).build();
    }

}