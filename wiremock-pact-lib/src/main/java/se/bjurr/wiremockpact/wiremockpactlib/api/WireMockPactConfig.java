package se.bjurr.wiremockpact.wiremockpactlib.api;

public class WireMockPactConfig {
  private String pactJsonFolder = "pact-json";
  private String providerDefaultValue = "the-provider";
  private String consumerDefaultValue = "the-consumer";

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

  public String getConsumerDefaultValue() {
    return this.consumerDefaultValue;
  }

  public String getPactJsonFolder() {
    return this.pactJsonFolder;
  }

  public String getProviderDefaultValue() {
    return this.providerDefaultValue;
  }

  public WireMockPactConfig setValuesOrKeepDefaults(final WireMockPactConfig config) {
    if (config.getConsumerDefaultValue() != null) {
      this.setConsumerDefaultValue(config.getConsumerDefaultValue());
    }

    if (config.getProviderDefaultValue() != null) {
      this.setProviderDefaultValue(config.getProviderDefaultValue());
    }

    if (config.getPactJsonFolder() != null) {
      this.setPactJsonFolder(config.getPactJsonFolder());
    }
    return this;
  }
}
