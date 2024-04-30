package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PactResponse {
  private int status;
  private Map<String, List<String>> headers;
  private PactBody body;

  public PactResponse() {}

  public PactResponse setBody(final PactBody body) {
    this.body = body;
    return this;
  }

  public PactResponse setHeaders(final Map<String, List<String>> headers) {
    this.headers = Map.copyOf(headers);
    return this;
  }

  public PactResponse setStatus(final int status) {
    this.status = status;
    return this;
  }

  public int getStatus() {
    return this.status;
  }

  public Map<String, List<String>> getHeaders() {
    return this.headers;
  }

  public PactBody getBody() {
    return this.body;
  }

  @Override
  public String toString() {
    return "PactResponse [status="
        + this.status
        + ", headers="
        + this.headers
        + ", body="
        + this.body
        + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.body, this.headers, this.status);
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
    final PactResponse other = (PactResponse) obj;
    return Objects.equals(this.body, other.body)
        && Objects.equals(this.headers, other.headers)
        && this.status == other.status;
  }
}
