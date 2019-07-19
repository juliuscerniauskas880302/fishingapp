package strategy;

import domain.Logbook;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class DatabaseSavingStrategy implements SavingStrategy {

    private EntityManager manager;

    public DatabaseSavingStrategy(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    @Transactional(rollbackOn = {SQLException.class}, dontRollbackOn = {SQLWarning.class})
    public void save(Logbook logbook) {
        if (!manager.contains(logbook)) {
            manager.persist(logbook);
        } else {
            manager.merge(logbook);
        }
    }

}
