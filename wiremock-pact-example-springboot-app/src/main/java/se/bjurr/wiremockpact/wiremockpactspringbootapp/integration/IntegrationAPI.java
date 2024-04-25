package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model.AnimalIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model.AnimalsIntegrationVO;

@Path("/")
public interface IntegrationAPI {
  @Path("/animals")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  AnimalsIntegrationVO getAnimals();

  @Path("/animals/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  AnimalIntegrationVO getAnimal(@PathParam("id") String id);
}
