package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.List;
import java.util.Objects;

public class Pact {
  private PactPacticipant consumer;
  private PactPacticipant provider;
  private List<PactInteraction> interactions;
  private PactMetadata metadata;

  public Pact() {}

  public Pact setConsumer(final PactPacticipant consumer) {
    this.consumer = consumer;
    return this;
  }

  public Pact setInteractions(final List<PactInteraction> interactions) {
    this.interactions = List.of(interactions.toArray(new PactInteraction[] {}));
    return this;
  }

  public Pact setProvider(final PactPacticipant provider) {
    this.provider = provider;
    return this;
  }

  public List<PactInteraction> getInteractions() {
    return this.interactions;
  }

  public PactPacticipant getConsumer() {
    return this.consumer;
  }

  public PactPacticipant getProvider() {
    return this.provider;
  }

  public Pact setMetadata(final PactMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  public PactMetadata getMetadata() {
    return this.metadata;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.consumer, this.interactions, this.metadata, this.provider);
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
    final Pact other = (Pact) obj;
    return Objects.equals(this.consumer, other.consumer)
        && Objects.equals(this.interactions, other.interactions)
        && Objects.equals(this.metadata, other.metadata)
        && Objects.equals(this.provider, other.provider);
  }

  @Override
  public String toString() {
    return "Pact [consumer="
        + this.consumer
        + ", provider="
        + this.provider
        + ", interactions="
        + this.interactions
        + ", metadata="
        + this.metadata
        + "]";
  }
}
