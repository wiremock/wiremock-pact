package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model.AnimalIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model.AnimalsIntegrationVO;

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

  @Path("/animals")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  void postAnimals(AnimalsIntegrationVO animals);

  @Path("/animals/{id}")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  void postAnimal(@PathParam("id") String id, AnimalIntegrationVO animal);
}
