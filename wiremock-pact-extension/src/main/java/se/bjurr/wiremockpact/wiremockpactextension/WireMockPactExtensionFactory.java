package se.bjurr.wiremockpact.wiremockpactextension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.github.tomakehurst.wiremock.extension.ExtensionFactory;
import com.github.tomakehurst.wiremock.extension.WireMockServices;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

public class WireMockPactExtensionFactory implements ExtensionFactory {
  private static final String WIREMOCK_PACT_SETTINGS_JSON = "wiremock-pact-settings.json";
  private static final String WIREMOCK_PACT_SETTINGS_BLOB = "pact-settings";
  private static final Logger LOG =
      Logger.getLogger(WireMockPactExtensionFactory.class.getSimpleName());

  @Override
  public List<Extension> create(final WireMockServices services) {
    try {
      final Optional<byte[]> wiremockSettings =
          services
              .getStores()
              .getBlobStore(WIREMOCK_PACT_SETTINGS_BLOB)
              .get(WIREMOCK_PACT_SETTINGS_JSON);
      WireMockPactConfig config;
      if (wiremockSettings.isPresent()) {
        config = new ObjectMapper().readValue(wiremockSettings.get(), WireMockPactConfig.class);
        LOG.info(
            "Found "
                + WIREMOCK_PACT_SETTINGS_JSON
                + " settings in "
                + WIREMOCK_PACT_SETTINGS_BLOB
                + " folder.");
      } else {
        LOG.info(
            "Did not find "
                + WIREMOCK_PACT_SETTINGS_JSON
                + " in "
                + WIREMOCK_PACT_SETTINGS_BLOB
                + " folder, using default settings.");
        config = WireMockPactConfig.builder();
      }
      final String prettyJson =
          new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(config);
      LOG.info("Using settings:\n" + prettyJson);
      return List.of(new WireMockPactExtension(config));
    } catch (final IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
