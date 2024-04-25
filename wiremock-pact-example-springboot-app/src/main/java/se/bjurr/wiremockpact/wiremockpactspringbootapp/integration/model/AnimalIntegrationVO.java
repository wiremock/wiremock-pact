package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.model;

public class AnimalIntegrationVO {
  private final String name;

  public AnimalIntegrationVO() {
    this.name = null;
  }

  public AnimalIntegrationVO(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
