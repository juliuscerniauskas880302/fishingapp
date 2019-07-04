package strategy;

import domain.Logbook;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

public class DatabaseSaveStrategy implements SaveStrategy {
    private EntityManager em;

    public DatabaseSaveStrategy(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Response create(Logbook logbook) {
        em.persist(logbook);
        return Response.ok("Logbook saved in database").build();
    }
}
