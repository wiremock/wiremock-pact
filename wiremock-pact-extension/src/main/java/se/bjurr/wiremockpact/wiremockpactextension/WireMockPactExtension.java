package se.bjurr.wiremockpact.wiremockpactextension;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ServeEventListener;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;
import se.bjurr.wiremockpact.wiremockpactlib.api.WiremockPactApi;

public class WireMockPactExtension implements ServeEventListener {

  private final WiremockPactApi wiremockPactApi;

  public WireMockPactExtension(final WireMockPactConfig config) {
    this.wiremockPactApi =
        WiremockPactApi.create(WireMockPactConfig.builder().setValuesOrKeepDefaults(config));
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
