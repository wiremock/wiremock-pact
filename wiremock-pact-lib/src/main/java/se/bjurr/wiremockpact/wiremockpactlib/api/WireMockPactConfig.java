package se.bjurr.wiremockpact.wiremockpactlib.api;

import java.util.ArrayList;
import java.util.List;

public class WireMockPactConfig {
  private String pactJsonFolder = "pact-json";
  private String providerDefaultValue = "the-provider";
  private List<String> providerStatesDefaultValue = new ArrayList<>();
  private String consumerDefaultValue = "the-consumer";
  private String includeRequestHeadersRegexp = "^(?!Host|User-Agent|Connection$).*";
  private String includeResponseHeadersRegexp = "^(?!Matched-Stub-Id$).*";

  public WireMockPactConfig() {}

  public static WireMockPactConfig builder() {
    return new WireMockPactConfig();
  }

  /** If no consumer found in mappings file, this value will be used. */
  public WireMockPactConfig setConsumerDefaultValue(final String value) {
    this.consumerDefaultValue = value;
    return this;
  }

  /** If no provider found in mappings file, this value will be used. */
  public WireMockPactConfig setProviderDefaultValue(final String value) {
    this.providerDefaultValue = value;
    return this;
  }

  /** Where to store PACT json files. */
  public WireMockPactConfig setPactJsonFolder(final String value) {
    this.pactJsonFolder = value;
    return this;
  }

  public WireMockPactConfig setIncludeRequestHeadersRegexp(
      final String includeRequestHeadersRegexp) {
    this.includeRequestHeadersRegexp = includeRequestHeadersRegexp;
    return this;
  }

  public WireMockPactConfig setIncludeResponseHeadersRegexp(
      final String includeResponseHeadersRegexp) {
    this.includeResponseHeadersRegexp = includeResponseHeadersRegexp;
    return this;
  }

  public WireMockPactConfig setProviderStatesDefaultValue(
      final List<String> providerStatesDefaultValue) {
    this.providerStatesDefaultValue = providerStatesDefaultValue;
    return this;
  }

  public List<String> getProviderStatesDefaultValue() {
    return this.providerStatesDefaultValue;
  }

  public String getConsumerDefaultValue() {
    return this.consumerDefaultValue;
  }

  public String getPactJsonFolder() {
    return this.pactJsonFolder;
  }

  public String getProviderDefaultValue() {
    return this.providerDefaultValue;
  }

  public String getIncludeRequestHeadersRegexp() {
    return this.includeRequestHeadersRegexp;
  }

  public String getIncludeResponseHeadersRegexp() {
    return this.includeResponseHeadersRegexp;
  }

  @Override
  public String toString() {
    return "WireMockPactConfig [pactJsonFolder="
        + this.pactJsonFolder
        + ", providerDefaultValue="
        + this.providerDefaultValue
        + ", providerStatesDefaultValue="
        + this.providerStatesDefaultValue
        + ", consumerDefaultValue="
        + this.consumerDefaultValue
        + ", includeRequestHeadersRegexp="
        + this.includeRequestHeadersRegexp
        + ", includeResponseHeadersRegexp="
        + this.includeResponseHeadersRegexp
        + "]";
  }
}
