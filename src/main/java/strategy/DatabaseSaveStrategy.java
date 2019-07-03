package strategy;

import domain.Logbook;

import javax.persistence.EntityManager;
import javax.transaction.TransactionalException;
import javax.ws.rs.core.Response;

public class DatabaseSaveStrategy implements SaveStrategy {
    private EntityManager em;

    public DatabaseSaveStrategy(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Response create(Logbook logbook) {
        em.getTransaction().begin();
        try {
            em.persist(logbook.getArrival());
            em.persist(logbook.getDeparture());
            em.persist(logbook.getEndFishing());
            logbook.getCatches().stream().forEach(em::persist);
            em.persist(logbook);
            em.getTransaction().commit();
            em.close();
        } catch (TransactionalException ex) {
            em.getTransaction().rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            em.close();
        }
        return Response.ok("Logbook saved in database").build();
    }
}
