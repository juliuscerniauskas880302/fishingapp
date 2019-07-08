package resource;

import service.PropertyLoaderDBService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/configs")
@Produces({MediaType.APPLICATION_JSON})
public class ConfigurationController {

    @Inject
    private PropertyLoaderDBService propertyLoaderDBService;

    @GET
    @Path("/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("key") final String key) {
        return propertyLoaderDBService.getValueByKey(key, "default");
    }
}
