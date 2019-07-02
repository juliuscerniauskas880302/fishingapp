package resource;

import domain.Catch;
import ejb.CatchEJB;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/catches")
public class CatchResource {

    @Inject
    private CatchEJB catchEJB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Catch> findAll() {
        return catchEJB.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Catch aCatch) {
        catchEJB.create(aCatch);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") final Long id, Catch aCatch) {
        catchEJB.update(id, aCatch);
    }

    @DELETE
    @Path("/{id}")
    public void remove(@PathParam("id") final Long id) {
        catchEJB.remove(id);
    }
}
