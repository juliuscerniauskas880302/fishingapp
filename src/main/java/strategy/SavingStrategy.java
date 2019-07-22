package strategy;

import domain.Logbook;

public interface SavingStrategy {

    void save(Logbook logbook) throws Exception;

}
