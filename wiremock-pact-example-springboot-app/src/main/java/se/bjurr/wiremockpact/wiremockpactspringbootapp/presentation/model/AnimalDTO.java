package se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model;

public class AnimalDTO {
  private final String name;

  public AnimalDTO(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
