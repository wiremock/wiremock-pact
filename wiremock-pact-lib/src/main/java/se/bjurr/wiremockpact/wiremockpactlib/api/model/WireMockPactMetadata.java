package se.bjurr.wiremockpact.wiremockpactlib.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WireMockPactMetadata {
  public static final String METADATA_ATTR = "wireMockPactSettings";
  private String provider;
  private List<String> providerStates;

  public WireMockPactMetadata() {
    this.provider = null;
    this.providerStates = new ArrayList<>();
  }

  public WireMockPactMetadata setProvider(final String provider) {
    this.provider = provider;
    return this;
  }

  public String getProvider() {
    return this.provider;
  }

  public WireMockPactMetadata setProviderStates(final List<String> providerStates) {
    this.providerStates = providerStates;
    return this;
  }

  public List<String> getProviderStates() {
    return this.providerStates;
  }

  @Override
  public String toString() {
    return "WireMockPactMetadata [provider="
        + this.provider
        + ", providerStates="
        + this.providerStates
        + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.provider, this.providerStates);
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
    return Objects.equals(this.provider, other.provider)
        && Objects.equals(this.providerStates, other.providerStates);
  }
}
