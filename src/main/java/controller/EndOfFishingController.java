package controller;

import domain.EndOfFishing;
import service.endOffFishing.EndOffFishingService;

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
    private EndOffFishingService endOffFishingService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public EndOfFishing getById(@PathParam("id") final String id) {
        return endOffFishingService.findById(id);

    }

    @GET
    public List<EndOfFishing> findAll() {
        return endOffFishingService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid EndOfFishing source) {
        return endOffFishingService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateById(@PathParam("id") final String id, @Valid EndOfFishing source) {
        endOffFishingService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") final String id) {
        endOffFishingService.deleteById(id);
    }
}
