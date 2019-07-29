package dao.logbook;

import common.ApplicationVariables;
import domain.enums.CommunicationType;
import domain.Logbook;
import io.xlate.inject.Property;
import io.xlate.inject.PropertyResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;
import strategy.DatabaseSavingStrategy;
import strategy.FileSavingStrategy;
import strategy.SavingStrategy;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
@SuppressWarnings("unchecked")
public class LogbookDAOImpl implements LogbookDAO {
    private static final Logger LOG = LogManager.getLogger(LogbookDAOImpl.class);
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
    private static final String FIND_ALL_INACTIVE_LOGBOOKS = "SELECT * FROM LOGBOOK L WHERE L.ENABLED IS FALSE;";

    @Inject
    @Property(name = "strategy.file.filePath",
            resource = @PropertyResource(ApplicationVariables.PROPERTIES_FILE_PATH),
            defaultValue = "C:/datafiles/satellite/notfound")
    private String FILE_PATH;

    private SavingStrategy savingStrategy;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Logbook> findByPort(String port) {
        return Optional.ofNullable(entityManager.createNativeQuery(FIND_ALL_LOGBOOKS_BY_PORT, Logbook.class)
                .setParameter(1, port)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findBySpecies(String species) {
        return Optional.ofNullable(entityManager.createNativeQuery(FIND_ALL_LOGBOOKS_BY_SPECIES, Logbook.class)
                .setParameter(1, species)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findByArrivalDateIn(String date1, String date2) {
        return Optional.ofNullable(entityManager.createNativeQuery(FIND_BY_ARRIVAL_DATE, Logbook.class)
                .setParameter(1, date1)
                .setParameter(2, date2)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findByDepartureDateIn(String date1, String date2) {
        return Optional.ofNullable(entityManager.createNativeQuery(FIND_BY_DEPARTURE_DATE, Logbook.class)
                .setParameter(1, date1)
                .setParameter(2, date2)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void saveAll(List<Logbook> logbooks) {
        logbooks.forEach(this::save);
    }

    @Override
    public List<Logbook> findAllInactiveLogbooks() {
        return Optional.ofNullable(entityManager.createNativeQuery(FIND_ALL_INACTIVE_LOGBOOKS, Logbook.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public Logbook findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(entityManager.find(Logbook.class, id))
                .orElse(null);
    }

    @Override
    public List<Logbook> findAll() {
        return Optional.ofNullable(entityManager.createNativeQuery(GET_ALL_LOGBOOKS, Logbook.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Logbook entity) {
        if (CommunicationType.SATELLITE.equals(entity.getCommunicationType())) {
            savingStrategy = new FileSavingStrategy(FILE_PATH);
        } else {
            savingStrategy = new DatabaseSavingStrategy(entityManager);
        }
        try {
            savingStrategy.save(entity);
            LOG.info("Logbook {} has been created using {} strategy.", entity.getId(), savingStrategy.getClass().getName());
        } catch (Exception ex) {
            LOG.info("Unable to create Logbook {} using {} strategy. {}.", entity.getId(), savingStrategy.getClass().getName(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(Logbook entity) throws ResourceNotFoundException, ResourceLockedException {
        entityManager.merge(entity);
        entityManager.flush();
    }

    @Override
    public void deleteById(String id) {
        Optional<Logbook> logbookOptional = Optional.ofNullable(entityManager.find(Logbook.class, id));
        if (logbookOptional.isPresent()) {
            entityManager.remove(logbookOptional.get());
        } else throw new ResourceNotFoundException("Cannot find Logbook with id: " + id);
    }
}
