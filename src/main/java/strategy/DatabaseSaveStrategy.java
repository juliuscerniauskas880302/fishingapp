package strategy;

import domain.Logbook;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

@Stateless
public class DatabaseSaveStrategy implements SaveStrategy {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Response create(Logbook logbook) {
        em.persist(logbook);
        return Response.ok("Logbook saved in database").build();
    }
}
