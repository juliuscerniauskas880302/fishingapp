package resource;

import domain.EndOfFishing;
import service.endoffishing.EndOfFishingService;

import javax.inject.Inject;
import javax.validation.Valid;
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

@Path("/end")
@Produces({MediaType.APPLICATION_JSON})
public class EndOfFishingController {
    @Inject
    private EndOfFishingService endOfFishingService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public EndOfFishing getById(@PathParam("id") final String id) {
        return endOfFishingService.findById(id);

    }

    @GET
    public List<EndOfFishing> findAll() {
        return endOfFishingService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid EndOfFishing source) {
        return endOfFishingService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateById(@PathParam("id") final String id, @Valid EndOfFishing source) {
        endOfFishingService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") final String id) {
        endOfFishingService.deleteById(id);
    }
}
