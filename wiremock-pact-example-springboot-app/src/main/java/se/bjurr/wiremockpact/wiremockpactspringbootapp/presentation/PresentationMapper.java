package se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation;

import java.util.List;
import org.springframework.stereotype.Service;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model.AnimalIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model.AnimalsIntegrationVO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model.AnimalDTO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model.AnimalsDTO;

@Service
public class PresentationMapper {

  public AnimalsDTO toAnimalsDTO(final AnimalsIntegrationVO animals) {
    final List<AnimalDTO> toAnimals =
        animals.getAnimals().stream().map(it -> this.toAnimalDTO(it)).toList();
    return new AnimalsDTO(toAnimals);
  }

  public AnimalDTO toAnimalDTO(final AnimalIntegrationVO it) {
    return new AnimalDTO(it.getName());
  }
}
