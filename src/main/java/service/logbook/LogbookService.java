package service.logbook;

import domain.Logbook;
import service.Dao;

import java.util.List;

public interface LogbookService extends Dao<Logbook, String> {
    List<Logbook> findByPort(String port);
    List<Logbook> findBySpecies(String species);
    List<Logbook> findByArrivalDate(String date1, String date2);
    List<Logbook> findByDepartureDate(String date1, String date2);

}
