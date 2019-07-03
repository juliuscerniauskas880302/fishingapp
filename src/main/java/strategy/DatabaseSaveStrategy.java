package strategy;

import camel.DataSaveRouteContext;
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
        em.persist(logbook.getArrival());
        em.persist(logbook.getDeparture());
        em.persist(logbook.getEndFishing());
        logbook.getCatches().stream().forEach(em::persist);
        em.persist(logbook);
        return Response.ok("Logbook saved in database").build();
    }
}
