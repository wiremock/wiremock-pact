package se.bjurr.wiremockpact.wiremockpactlib.api.model;

import java.util.Objects;

public class MetadataModelWireMockPactSettings {
  private final String provider;

  public MetadataModelWireMockPactSettings() {
    this.provider = null;
  }

  public MetadataModelWireMockPactSettings(final String provider) {
    this.provider = provider;
  }

  public String getProvider() {
    return this.provider;
  }

  @Override
  public String toString() {
    return "MetadataModelWireMockPactSettings [provider=" + this.provider + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.provider);
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
    final MetadataModelWireMockPactSettings other = (MetadataModelWireMockPactSettings) obj;
    return Objects.equals(this.provider, other.provider);
  }
}
