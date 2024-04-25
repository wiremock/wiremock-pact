package se.bjurr.wiremockpact.wiremockpactspringbootapp.logic;

import org.springframework.stereotype.Service;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.IntegrationDelegatingService;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model.AnimalIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model.AnimalsIntegrationVO;

@Service
public class AnimalsLogicDelegatingService {

  private final IntegrationDelegatingService integrationService;

  public AnimalsLogicDelegatingService(final IntegrationDelegatingService integrationService) {
    this.integrationService = integrationService;
  }

  public AnimalsIntegrationVO getAnimals() {
    return this.integrationService.getAnimals();
  }

  public AnimalIntegrationVO getAnimal(final String id) {
    return this.integrationService.getAnimal(id);
  }
}
