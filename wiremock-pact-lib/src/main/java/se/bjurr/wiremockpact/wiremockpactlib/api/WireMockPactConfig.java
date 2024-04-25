package se.bjurr.wiremockpact.wiremockpactlib.api;

public final class WireMockPactConfig {
  private String pactJsonFolder;
  private String providerMetadataJsonPath;
  private String providerDefaultValue;
  private String consumerDefaultValue;
  private String consumerMetadataJsonPath;

  private WireMockPactConfig() {}

  public static WireMockPactConfig builder() {
    return new WireMockPactConfig();
  }

  /** If no consumer found in mappings file, this value will be used. */
  public WireMockPactConfig withConsumerDefaultValue(final String value) {
    this.consumerDefaultValue = value;
    return this;
  }

  /**
   * This JSONPath will be evaluated against the Metadata field in mappings json to get the
   * consumer.
   */
  public WireMockPactConfig withConsumerMetadataJsonPath(final String value) {
    this.consumerMetadataJsonPath = value;
    return this;
  }

  /** If no provider found in mappings file, this value will be used. */
  public WireMockPactConfig withProviderDefaultValue(final String value) {
    this.providerDefaultValue = value;
    return this;
  }

  /**
   * This JSONPath will be evaluated against the Metadata field in mappings json to get the
   * provider.
   */
  public WireMockPactConfig withProviderMetadataJsonPath(final String value) {
    this.providerMetadataJsonPath = value;
    return this;
  }

  /** Where to store PACT json files. */
  public WireMockPactConfig withPactJsonFolder(final String value) {
    this.pactJsonFolder = value;
    return this;
  }

  public String getConsumerDefaultValue() {
    return this.consumerDefaultValue;
  }

  public String getConsumerMetadataJsonPath() {
    return this.consumerMetadataJsonPath;
  }

  public String getPactJsonFolder() {
    return this.pactJsonFolder;
  }

  public String getProviderDefaultValue() {
    return this.providerDefaultValue;
  }

  public String getProviderMetadataJsonPath() {
    return this.providerMetadataJsonPath;
  }
}
