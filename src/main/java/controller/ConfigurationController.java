package controller;

import domain.config.Configuration;
import service.config.ConfigService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/configs")
@Produces({MediaType.APPLICATION_JSON})
public class ConfigurationController {

    @Inject
    private ConfigService configService;

    @GET
    @Path("/{key}/{defaultValue}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getByKey(
            @PathParam("key") final String key,
            @PathParam("defaultValue") final String defaultValue) {
        return configService.getValueByKey(key, defaultValue);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Configuration> getAll() {
        return configService.getAll();
    }

    @DELETE
    @Path("/{key}")
    public void delete(@PathParam("key") final String key) {
        configService.delete(key);
    }

    @POST
    public void add(@Valid Configuration configuration) {
        configService.add(configuration);
    }

    @PUT
    @Path("/{key}/{value}/{description}")
    public void update(
            @PathParam("key") final String key,
            @PathParam("value") final String value,
            @PathParam("description") final String description) {
        configService.update(key, value, description);
    }

}
