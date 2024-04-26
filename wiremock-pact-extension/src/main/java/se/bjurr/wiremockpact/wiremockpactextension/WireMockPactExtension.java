package se.bjurr.wiremockpact.wiremockpactextension;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ServeEventListener;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import se.bjurr.wiremockpact.wiremockpactextension.support.BaseSetupJunitExtension;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

public class WireMockPactExtension extends BaseSetupJunitExtension implements ServeEventListener {

  private final WireMockPactApi wiremockPactApi;

  public WireMockPactExtension(final WireMockPactConfig config) {
    super();
    this.wiremockPactApi = WireMockPactApi.create(config);
  }

  @Override
  public String getName() {
    return WireMockPactExtension.class.getSimpleName();
  }

  @Override
  public void afterComplete(final ServeEvent serveEvent, final Parameters parameters) {
    this.wiremockPactApi.addServeEvent(serveEvent);
  }

  @Override
  public void setup() {
    this.wiremockPactApi.clearAllSaved();
  }

  @Override
  public void close() throws Throwable {
    this.wiremockPactApi.saveAll();
  }
}
