package dao.logbook;

import dao.AbstractDAO;
import domain.Logbook;

import java.util.List;

public interface LogbookDAO extends AbstractDAO<Logbook, String> {

    List<Logbook> findByPort(String port);

    List<Logbook> findBySpecies(String species);

    List<Logbook> findByArrivalDateIn(String date1, String date2);

    List<Logbook> findByDepartureDateIn(String date1, String date2);

    void saveAll(List<Logbook> logbooks) throws Exception;
}
