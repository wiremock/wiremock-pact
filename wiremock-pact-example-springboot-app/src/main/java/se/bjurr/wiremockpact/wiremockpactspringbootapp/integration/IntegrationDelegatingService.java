package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.IntegrationAPI;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model.AnimalIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model.AnimalsIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.clientfactory.RestEasyClientFactory;

@Repository
public class IntegrationDelegatingService {
  private final IntegrationAPI api;

  public IntegrationDelegatingService(@Value("${basepath}") final String basePath) {
    this.api = RestEasyClientFactory.create(IntegrationAPI.class, basePath);
  }

  public AnimalsIntegrationVO getAnimals() {
    return this.api.getAnimals();
  }

  public AnimalIntegrationVO getAnimal(final String id) {
    return this.api.getAnimal(id);
  }
  
  public void postAnimals(final AnimalsIntegrationVO animals) {
	     this.api.postAnimals(animals);
  }

public void postAnimal(final AnimalIntegrationVO animal) {
    this.api.postAnimal(animal.getId(),animal);
  }
}
