package se.bjurr.wiremockpact.wiremockpactlib.api;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ServeEventListener;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

public class WireMockPactExtension implements ServeEventListener {

  private final WiremockPactApi wiremockPactApi;

  public WireMockPactExtension() {
    this.wiremockPactApi =
        WiremockPactApi.create(
            WireMockPactConfig.builder()
                .withConsumerDefaultValue("the-consumer")
                .withProviderDefaultValue("the-provider")
                .withPactJsonFolder("build/pact-json"));
  }

  @Override
  public String getName() {
    return WireMockPactExtension.class.getSimpleName();
  }

  @Override
  public void afterComplete(final ServeEvent serveEvent, final Parameters parameters) {
    this.wiremockPactApi.toPact(serveEvent);
  }
}
