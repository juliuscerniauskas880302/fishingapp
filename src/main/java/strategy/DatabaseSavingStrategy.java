package strategy;

import domain.Logbook;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

public class DatabaseSavingStrategy implements SavingStrategy {
    private EntityManager manager;

    public DatabaseSavingStrategy(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Response save(Logbook logbook) {
        if (!manager.contains(logbook)) {
            manager.persist(logbook);
            return Response.status(Response.Status.OK).build();
        } else {
            manager.merge(logbook);
            return Response.status(Response.Status.OK).build();
        }
    }
}
