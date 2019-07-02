package resource;

import domain.EndFishing;
import ejb.EndFishingEJB;

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
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ends")
public class EndFishingResource {
    @Inject
    private EndFishingEJB endFishingEJB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EndFishing> findAll() {
        return endFishingEJB.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public EndFishing findById(@PathParam("id") final Long id) {
        return endFishingEJB.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(EndFishing departure) {
        return endFishingEJB.create(departure);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final Long id, EndFishing departure) {
        return endFishingEJB.update(id, departure);
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") final Long id) {
        return endFishingEJB.remove(id);
    }
}
