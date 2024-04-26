package se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import se.bjurr.wiremockpact.wiremockpactspringbootapp.logic.AnimalsLogicDelegatingService;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model.AnimalDTO;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model.AnimalsDTO;

@RestController
public class ControllerImpl {
  private final AnimalsLogicDelegatingService logic;
  private final PresentationMapper mapper;

  public ControllerImpl(
      final AnimalsLogicDelegatingService logic, final PresentationMapper mapper) {
    this.logic = logic;
    this.mapper = mapper;
  }

  @GetMapping("/animals")
  public AnimalsDTO getAnimals() {
    return this.mapper.toAnimalsDTO(this.logic.getAnimals());
  }

  @GetMapping("/animals/{id}")
  public AnimalDTO getAnimal(@PathVariable("id") final String id) {
    return this.mapper.toAnimalDTO(this.logic.getAnimal(id));
  }

  @PostMapping("/animals")
  public void getAnimals(@RequestBody final AnimalsDTO animals) {
  this.logic.postAnimals(this.mapper.toAnimals(animals));
  }

  @PostMapping("/animals/{id}")
  public void getAnimal(@PathVariable("id") final String id, @RequestBody final AnimalDTO animal) {
    this.logic.postAnimal(this.mapper.toAnimal(id, animal));
  }
}
