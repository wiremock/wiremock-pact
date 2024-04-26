package se.bjurr.wiremockpact.wiremockpactspringbootapp.presentation.model;

public class AnimalDTO {
  private final String id;
  private final String name;

  public AnimalDTO() {
    this.id = null;
    this.name = null;
  }

  public AnimalDTO(final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }
}
