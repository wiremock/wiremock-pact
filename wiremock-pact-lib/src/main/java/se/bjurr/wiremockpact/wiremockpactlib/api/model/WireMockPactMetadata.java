package se.bjurr.wiremockpact.wiremockpactlib.api.model;

import java.util.Objects;

public class WireMockPactMetadata {
  public static final String METADATA_ATTR = "wireMockPactSettings";
  private String provider;

  public WireMockPactMetadata() {
    this.provider = null;
  }

  public WireMockPactMetadata setProvider(final String provider) {
    this.provider = provider;
    return this;
  }

  public String getProvider() {
    return this.provider;
  }

  @Override
  public String toString() {
    return "WireMockPactMetadata [provider=" + this.provider + "]";
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
    final WireMockPactMetadata other = (WireMockPactMetadata) obj;
    return Objects.equals(this.provider, other.provider);
  }
}
