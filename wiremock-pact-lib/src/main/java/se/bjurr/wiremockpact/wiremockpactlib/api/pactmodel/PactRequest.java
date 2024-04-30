package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class PactRequest {
  private String method;
  private String path;
  private String query;
  private Map<String, List<String>> headers;
  private PactBody body;

  public PactRequest() {}

  public PactRequest setBody(final PactBody body) {
    this.body = body;
    return this;
  }

  public PactRequest setHeaders(final Map<String, List<String>> headers) {
    this.headers = Collections.unmodifiableMap(new TreeMap<>(Map.copyOf(headers)));
    return this;
  }

  public PactRequest setMethod(final String method) {
    this.method = method;
    return this;
  }

  public PactRequest setPath(final String path) {
    this.path = path;
    return this;
  }

  public PactRequest setQuery(final String query) {
    this.query = query;
    return this;
  }

  public String getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

  public String getQuery() {
    return this.query;
  }

  public Map<String, List<String>> getHeaders() {
    return this.headers;
  }

  public PactBody getBody() {
    return this.body;
  }

  @Override
  public String toString() {
    return "PactRequest [method="
        + this.method
        + ", path="
        + this.path
        + ", query="
        + this.query
        + ", headers="
        + this.headers
        + ", body="
        + this.body
        + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.body, this.headers, this.method, this.path, this.query);
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
    final PactRequest other = (PactRequest) obj;
    return Objects.equals(this.body, other.body)
        && Objects.equals(this.headers, other.headers)
        && Objects.equals(this.method, other.method)
        && Objects.equals(this.path, other.path)
        && Objects.equals(this.query, other.query);
  }
}
