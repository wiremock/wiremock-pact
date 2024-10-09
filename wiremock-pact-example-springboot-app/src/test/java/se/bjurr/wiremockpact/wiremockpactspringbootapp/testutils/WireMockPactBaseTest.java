package se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.WireMockConfigurationCustomizer;
import se.bjurr.wiremockpact.wiremockpactextensionjunit5.WireMockPactExtension;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

@EnableWireMock({@ConfigureWireMock(configurationCustomizers = {WireMockPactBaseTest.class})})
public class WireMockPactBaseTest implements WireMockConfigurationCustomizer {
  @RegisterExtension
  static WireMockPactExtension WIREMOCK_PACT_EXTENSION =
      new WireMockPactExtension(
          WireMockPactConfig.builder() //
              .setConsumerDefaultValue("WireMockPactExample") //
              .setProviderDefaultValue("UnknownProvider") //
              .setPactJsonFolder("src/test/resources/pact-json"));

  @Override
  public void customize(
      final WireMockConfiguration configuration, final ConfigureWireMock options) {
    configuration.extensions(WIREMOCK_PACT_EXTENSION);
  }
}
