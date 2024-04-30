package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

import java.util.Objects;

/**
 * Some logic implemented here:
 * https://github.com/pact-foundation/pact-jvm/blob/4752365ad30c9ba5640f37578d8b4a7b673d3653/core/model/src/main/kotlin/au/com/dius/pact/core/model/OptionalBody.kt#L1
 */
public class PactBody {

  private String content;
  private String contentType;
  private String encoded;

  public PactBody() {}

  public PactBody setContent(final String content) {
    this.content = content;
    return this;
  }

  public String getContent() {
    return this.content;
  }

  public PactBody setContentType(final String contentType) {
    this.contentType = contentType;
    return this;
  }

  public PactBody setEncoded(final String encoded) {
    this.encoded = encoded;
    return this;
  }

  public String getContentType() {
    return this.contentType;
  }

  public String getEncoded() {
    return this.encoded;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.content, this.contentType, this.encoded);
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
    final PactBody other = (PactBody) obj;
    return Objects.equals(this.content, other.content)
        && Objects.equals(this.contentType, other.contentType)
        && Objects.equals(this.encoded, other.encoded);
  }

  @Override
  public String toString() {
    return "PactBody [content="
        + this.content
        + ", contentType="
        + this.contentType
        + ", encoded="
        + this.encoded
        + "]";
  }
}
