package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.Objects;

public class PactMetadataVersion {
  private String version;

  public PactMetadataVersion() {}

  public PactMetadataVersion setVersion(final String version) {
    this.version = version;
    return this;
  }

  public String getVersion() {
    return this.version;
  }

  @Override
  public String toString() {
    return "PactMetadataVersion [version=" + this.version + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.version);
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
    final PactMetadataVersion other = (PactMetadataVersion) obj;
    return Objects.equals(this.version, other.version);
  }
}
