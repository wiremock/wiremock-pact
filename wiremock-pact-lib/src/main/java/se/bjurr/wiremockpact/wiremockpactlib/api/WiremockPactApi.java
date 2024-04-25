package se.bjurr.wiremockpact.wiremockpactlib.api;

import au.com.dius.pact.core.model.Consumer;
import au.com.dius.pact.core.model.IRequest;
import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.OptionalBody;
import au.com.dius.pact.core.model.OptionalBody.State;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.Provider;
import au.com.dius.pact.core.model.SynchronousRequestResponse;
import au.com.dius.pact.core.model.V4Interaction.SynchronousHttp;
import au.com.dius.pact.core.model.V4Pact;
import com.github.tomakehurst.wiremock.http.LoggedResponse;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class WiremockPactApi {
  private static final Logger LOG = Logger.getLogger(WiremockPactApi.class.getSimpleName());
  private final WireMockPactConfig config;
  private final List<ServeEvent> serveEvents = new ArrayList<>();

  private WiremockPactApi(final WireMockPactConfig config) {
    this.config = config;
  }

  public static WiremockPactApi create(final WireMockPactConfig config) {
    return new WiremockPactApi(config);
  }

  /** Record all given Wiremock requests to PACT. */
  public void toPact(final ServeEvent serveEvent) {
    this.serveEvents.add(serveEvent);
    LOG.info(
        "Saving " + serveEvent.getRequest().getUrl() + " to " + this.config.getPactJsonFolder());
    this.saveAll();
  }

  private void saveAll() {
    final Consumer consumer = new Consumer(this.config.getConsumerDefaultValue());
    final Provider provider = new Provider(this.config.getProviderDefaultValue());
    final V4Pact v4 = new V4Pact(consumer, provider);
    for (final ServeEvent serveEvent : this.serveEvents) {
      final LoggedRequest request = serveEvent.getRequest();
      final LoggedResponse response = serveEvent.getResponse();
      final Interaction interaction =
          new SynchronousHttp(
              request.getMethod().getName()
                  + " "
                  + request.getUrl()
                  + " -> "
                  + response.getStatus());
      final SynchronousRequestResponse asSynchronousRequestResponse =
          interaction.asSynchronousRequestResponse();
      final IRequest pactRequest = asSynchronousRequestResponse.getRequest();
      pactRequest.setMethod(request.getMethod().getName());
      pactRequest.setPath(request.getUrl());
      pactRequest.setBody(new OptionalBody(State.PRESENT, request.getBody()));
      v4.getInteractions().add(interaction);
    }

    LOG.fine("Saving " + v4 + " to " + this.config.getPactJsonFolder());
    v4.write(this.config.getPactJsonFolder(), PactSpecVersion.V4);
  }
}
