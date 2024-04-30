package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PactInteraction implements Comparable<PactInteraction> {
  private String description = "";
  private String key;
  private boolean pending;
  private PactRequest request;
  private PactResponse response;
  private List<PactProviderState> providerStates = new ArrayList<PactProviderState>();
  private String type = PactInteractionType.SYNC_HTTP.getTypeString();

  public PactInteraction() {}

  public PactInteraction setDescription(final String description) {
    this.description = description;
    return this;
  }

  public PactInteraction setRequest(final PactRequest request) {
    this.request = request;
    return this;
  }

  public PactInteraction setResponse(final PactResponse response) {
    this.response = response;
    return this;
  }

  public PactInteraction setProviderStates(final List<PactProviderState> providerStates) {
    this.providerStates =
        List.of(providerStates.stream().sorted().toList().toArray(new PactProviderState[] {}));
    return this;
  }

  public PactInteraction setType(final String type) {
    this.type = type;
    return this;
  }

  public String getType() {
    return this.type;
  }

  public PactResponse getResponse() {
    return this.response;
  }

  public PactRequest getRequest() {
    return this.request;
  }

  public String getDescription() {
    return this.description;
  }

  public List<PactProviderState> getProviderStates() {
    return this.providerStates;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }

  public PactInteraction setPending(final boolean pending) {
    this.pending = pending;
    return this;
  }

  public boolean isPending() {
    return this.pending;
  }

  @Override
  public int compareTo(final PactInteraction o) {
    return (this.description + this.providerStates.toString())
        .compareTo(o.description + o.providerStates.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.description,
        this.key,
        this.pending,
        this.providerStates,
        this.request,
        this.response,
        this.type);
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
    final PactInteraction other = (PactInteraction) obj;
    return Objects.equals(this.description, other.description)
        && Objects.equals(this.key, other.key)
        && this.pending == other.pending
        && Objects.equals(this.providerStates, other.providerStates)
        && Objects.equals(this.request, other.request)
        && Objects.equals(this.response, other.response)
        && this.type == other.type;
  }

  @Override
  public String toString() {
    return "PactInteraction [description="
        + this.description
        + ", key="
        + this.key
        + ", pending="
        + this.pending
        + ", request="
        + this.request
        + ", response="
        + this.response
        + ", providerStates="
        + this.providerStates
        + ", type="
        + this.type
        + "]";
  }
}
