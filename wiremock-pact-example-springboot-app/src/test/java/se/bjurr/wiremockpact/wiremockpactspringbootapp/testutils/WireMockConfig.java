package se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.WireMockConfigurationCustomizer;

public class WireMockConfig implements WireMockConfigurationCustomizer {

  @Override
  public void customize(
      final WireMockConfiguration configuration, final ConfigureWireMock options) {
    configuration.extensionScanningEnabled(true);
  }
}
