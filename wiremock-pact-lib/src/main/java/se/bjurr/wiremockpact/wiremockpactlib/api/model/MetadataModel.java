package se.bjurr.wiremockpact.wiremockpactlib.api.model;

import java.util.Objects;

public class MetadataModel {
  private WireMockPactMetadata wireMockPactSettings;

  public MetadataModel() {}

  public MetadataModel(final WireMockPactMetadata wireMockPactSettings) {
    this.wireMockPactSettings = wireMockPactSettings;
  }

  public WireMockPactMetadata getWireMockPactSettings() {
    return this.wireMockPactSettings;
  }

  @Override
  public String toString() {
    return "MetadataModel [wireMockPactSettings=" + this.wireMockPactSettings + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.wireMockPactSettings);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final MetadataModel other = (MetadataModel) obj;
    return Objects.equals(this.wireMockPactSettings, other.wireMockPactSettings);
  }
}
