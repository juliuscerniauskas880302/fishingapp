package strategy;

import domain.Logbook;

import javax.persistence.EntityManager;

public class DatabaseSavingStrategy implements SavingStrategy {
    private EntityManager manager;

    public DatabaseSavingStrategy(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void save(Logbook logbook) {
        if (!manager.contains(logbook)) {
            manager.persist(logbook);
        } else {
            manager.merge(logbook);
        }
    }
}
