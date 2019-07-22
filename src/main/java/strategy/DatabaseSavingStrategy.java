package strategy;

import domain.Logbook;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class DatabaseSavingStrategy implements SavingStrategy {

    private EntityManager manager;

    public DatabaseSavingStrategy(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    @Transactional
    public void save(Logbook logbook) throws Exception {
        manager.persist(logbook);
    }
}
