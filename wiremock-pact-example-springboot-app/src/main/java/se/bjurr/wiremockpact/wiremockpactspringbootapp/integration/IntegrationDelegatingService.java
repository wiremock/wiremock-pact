package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model.AnimalsIntegrationVO;

@Repository
public class IntegrationDelegatingService {
  private final IntegrationAPI api;

  public IntegrationDelegatingService(@Value("${basepath}") final String basePath) {
    this.api = RestEasyClientFactory.create(IntegrationAPI.class, basePath);
  }

  public AnimalsIntegrationVO getAnimals() {
    return this.api.getAnimals();
  }
}
