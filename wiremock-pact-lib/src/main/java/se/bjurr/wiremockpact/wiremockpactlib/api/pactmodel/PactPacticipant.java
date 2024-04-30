package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.Objects;

public class PactPacticipant {
  private String name;

  public PactPacticipant() {}

  public PactPacticipant setName(final String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
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
    final PactPacticipant other = (PactPacticipant) obj;
    return Objects.equals(this.name, other.name);
  }

  @Override
  public String toString() {
    return "Pacticipant [name=" + this.name + "]";
  }
}
