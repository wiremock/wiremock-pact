package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.Objects;

public class PactMetadata {
  private PactMetadataVersion pactSpecification;
  private String tool;

  public PactMetadata() {}

  public PactMetadata setPactSpecification(final PactMetadataVersion pactSpecification) {
    this.pactSpecification = pactSpecification;
    return this;
  }

  public PactMetadata setTool(final String tool) {
    this.tool = tool;
    return this;
  }

  public PactMetadataVersion getPactSpecification() {
    return this.pactSpecification;
  }

  public String getTool() {
    return this.tool;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pactSpecification, this.tool);
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
    final PactMetadata other = (PactMetadata) obj;
    return Objects.equals(this.pactSpecification, other.pactSpecification)
        && Objects.equals(this.tool, other.tool);
  }

  @Override
  public String toString() {
    return "PactMetadata [pactSpecification="
        + this.pactSpecification
        + ", tool="
        + this.tool
        + "]";
  }
}
