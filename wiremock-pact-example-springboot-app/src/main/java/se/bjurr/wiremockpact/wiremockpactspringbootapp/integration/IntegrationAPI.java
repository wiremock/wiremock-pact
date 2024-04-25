package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model.AnimalsIntegrationVO;

@Path("/")
public interface IntegrationAPI {
  @Path("/animals")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  AnimalsIntegrationVO getAnimals();
}
