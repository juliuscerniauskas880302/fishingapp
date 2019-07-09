package service.logbook;

import domain.Logbook;
import service.Dao;

import java.util.List;

public interface LogbookService extends Dao<Logbook, String> {
    List<Logbook> findByPort(String port);
    List<Logbook> findBySpecies(String species);
}
