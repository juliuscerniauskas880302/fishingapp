package service.logbook;

import common.ApplicationVariables;
import domain.Catch;
import domain.CommunicationType;
import domain.Logbook;
import io.xlate.inject.Property;
import io.xlate.inject.PropertyResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;
import strategy.DatabaseSavingStrategy;
import strategy.FileSavingStrategy;
import strategy.SavingStrategy;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
    public Logbook findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(manager.find(Logbook.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Logbook with id: " + id));
    }

    @Override
    public List<Logbook> findAll() {
        return Optional.ofNullable(manager.createNativeQuery(GET_ALL_LOGBOOKS, Logbook.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public void save(Logbook source) throws Exception {
        if (CommunicationType.SATELLITE.equals(source.getCommunicationType())) {
            savingStrategy = new FileSavingStrategy(FILE_PATH);
        } else {
            savingStrategy = new DatabaseSavingStrategy(manager);
        }
        try {
            savingStrategy.save(source);
            LOG.info("Logbook {} has been created using {} strategy.", source.getId(), savingStrategy.getClass().getName());
        } catch (Exception ex) {
            LOG.info("Unable to create Logbook {} using {} strategy. {}.", source.getId(), savingStrategy.getClass().getName(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public void update(Logbook source, String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook -> {
            logbook.setCommunicationType(source.getCommunicationType());
            logbook.getArrival().setPort(source.getArrival().getPort());
            logbook.getArrival().setDate(source.getArrival().getDate());
            for (Catch aCatch : source.getCatches()) {
                if (!logbook.getCatches().contains(aCatch)) {
                    logbook.getCatches().add(aCatch);
                }
            }
            logbook.getDeparture().setPort(source.getDeparture().getPort());
            logbook.getDeparture().setDate(source.getDeparture().getDate());
            logbook.getEndOfFishing().setDate(source.getEndOfFishing().getDate());
            manager.merge(logbook);
            try {
                manager.flush();
                LOG.info("Logbook {} has been updated.", id);
            } catch (OptimisticLockException ex) {
                LOG.error("Logbook {} is locked.", id);
                throw new ResourceLockedException("Logbook resource with id: " + id + " is locked.");
            }
        });
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook -> {
            manager.remove(logbook);
            LOG.info("Logbook {} has been deleted.", id);
        });
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
    @Transactional
    public void saveAll(List<Logbook> logbooks) throws Exception {
        logbooks.forEach(source -> {
            try {
                save(source);
            } catch (Exception e) {
            }
        });
    }
}