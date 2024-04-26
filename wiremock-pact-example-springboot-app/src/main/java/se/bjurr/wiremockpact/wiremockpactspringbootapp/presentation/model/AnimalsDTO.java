package se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model;

import java.util.List;

public class AnimalsDTO {
  private final List<AnimalDTO> animals;
  
  public AnimalsDTO() {
	this.animals = null;
}

  public AnimalsDTO(final List<AnimalDTO> animals) {
    this.animals = animals;
  }

  public List<AnimalDTO> getAnimals() {
    return this.animals;
  }
}
