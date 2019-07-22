package service.logbook;

import domain.Logbook;
import service.GenericDAO;

import java.util.List;

public interface LogbookService extends GenericDAO<Logbook, String> {
    List<Logbook> findByPort(String port);

    List<Logbook> findBySpecies(String species);

    List<Logbook> findByArrivalDateIn(String date1, String date2);

    List<Logbook> findByDepartureDateIn(String date1, String date2);

    void saveAll(List<Logbook> logbooks) throws Exception;
}
