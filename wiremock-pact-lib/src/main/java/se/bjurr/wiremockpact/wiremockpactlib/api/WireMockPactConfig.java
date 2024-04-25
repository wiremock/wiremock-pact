package se.bjurr.wiremockpact.wiremockpactlib.api;

public class WireMockPactConfig {
  private String pactJsonFolder = "build/pact-json";
  private String providerMetadataJsonPath;
  private String providerDefaultValue = "the-provider";
  private String consumerDefaultValue = "the-consumer";
  private String consumerMetadataJsonPath;

  public WireMockPactConfig() {}

  public static WireMockPactConfig builder() {
    return new WireMockPactConfig();
  }

  /** If no consumer found in mappings file, this value will be used. */
  public WireMockPactConfig setConsumerDefaultValue(final String value) {
    this.consumerDefaultValue = value;
    return this;
  }

  /**
   * This JSONPath will be evaluated against the Metadata field in mappings json to get the
   * consumer.
   */
  public WireMockPactConfig setConsumerMetadataJsonPath(final String value) {
    this.consumerMetadataJsonPath = value;
    return this;
  }

  /** If no provider found in mappings file, this value will be used. */
  public WireMockPactConfig setProviderDefaultValue(final String value) {
    this.providerDefaultValue = value;
    return this;
  }

  /**
   * This JSONPath will be evaluated against the Metadata field in mappings json to get the
   * provider.
   */
  public WireMockPactConfig setProviderMetadataJsonPath(final String value) {
    this.providerMetadataJsonPath = value;
    return this;
  }

  /** Where to store PACT json files. */
  public WireMockPactConfig setPactJsonFolder(final String value) {
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

  public WireMockPactConfig setValuesOrKeepDefaults(final WireMockPactConfig config) {
    if (config.getConsumerDefaultValue() == null) {
      this.setConsumerDefaultValue(config.getConsumerDefaultValue());
    }

    if (config.getProviderDefaultValue() == null) {
      this.setProviderDefaultValue(config.getProviderDefaultValue());
    }

    if (config.getPactJsonFolder() == null) {
      this.setPactJsonFolder(config.getPactJsonFolder());
    }
    return this;
  }
}
