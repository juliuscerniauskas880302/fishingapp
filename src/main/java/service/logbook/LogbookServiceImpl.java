package service.logbook;

import domain.Logbook;
import enums.CommunicationType;
import strategy.DatabaseSavingStrategy;
import strategy.FileSavingStrategy;
import strategy.SavingStrategy;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateful
public class LogbookServiceImpl implements LogbookService {
    private static final String FIND_ALL_LOGBOOKS_BY_SPECIES = "SELECT DISTINCT (LOGBOOK.*)" +
            " FROM LOGBOOK " +
            " LEFT JOIN LOGBOOK_CATCH LC ON LOGBOOK.ID = LC.LOGBOOK_ID" +
            " LEFT JOIN CATCH C ON LC.CATCHES_ID = C.ID" +
            " WHERE C.VARIETY LIKE :searchParam group by LOGBOOK.ID";
    private static final String GET_ALL_LOGBOOKS = "SELECT * FROM LOGBOOK";
    private static final String NOT_WORKING = "SELECT *" +
            " FROM LOGBOOK" +
            " LEFT JOIN LOGBOOK_CATCH LC ON LOGBOOK.ID = LC.LOGBOOK_ID" +
            " LEFT JOIN CATCH C ON LC.CATCHES_ID = C.ID" +
            " WHERE C.VARIETY LIKE :searchParam";
    private static final String FIND_ALL_LOGBOOKS_BY_PORT = "SELECT * FROM LOGBOOK" +
            " LEFT JOIN ARRIVAL A ON LOGBOOK.ARRIVAL_ID = A.ID" +
            " LEFT JOIN DEPARTURE D ON LOGBOOK.DEPARTURE_ID = D.ID" +
            " WHERE A.PORT LIKE :searchParam OR D.PORT LIKE :searchParam";

    private static final String FIND_BY_DEPARTURE_DATE = "SELECT LOGBOOK.* FROM LOGBOOK" +
            " INNER JOIN DEPARTURE A on LOGBOOK.DEPARTURE_ID = A.ID" +
            " WHERE DATE BETWEEN ?1 AND ?2";
    private static final String FIND_BY_ARRIVAL_DATE = "SELECT LOGBOOK.* FROM LOGBOOK" +
            " INNER JOIN ARRIVAL A on LOGBOOK.ARRIVAL_ID = A.ID" +
            " WHERE DATE BETWEEN ?1 AND ?2";

    private static final String FILE_PATH = "C:\\datafiles\\satellite\\";

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
        return savingStrategy.save(source);
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
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook ->
                manager.remove(logbook));
    }

    @Override
    public List<Logbook> findByPort(String port) {
        String search = "%" + port;
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_LOGBOOKS_BY_PORT, Logbook.class)
                .setParameter("searchParam", search)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findBySpecies(String species) {
        String search = "%" + species;
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_LOGBOOKS_BY_SPECIES, Logbook.class)
                .setParameter("searchParam", search)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findByArrivalDate(String date1, String date2) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_BY_ARRIVAL_DATE, Logbook.class)
                .setParameter(1, date1)
                .setParameter(2, date2)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public List<Logbook> findByDepartureDate(String date1, String date2) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_BY_DEPARTURE_DATE, Logbook.class)
                .setParameter(1, date1)
                .setParameter(2, date2)
                .getResultList()).orElse(Collections.emptyList());
    }
}