package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.api.model;

public class AnimalIntegrationVO {
  private final String id;
  private final String name;

  public AnimalIntegrationVO() {
    this.id = null;
    this.name = null;
  }

  public AnimalIntegrationVO(final String id, final String name) {
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
