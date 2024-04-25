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
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.util.ArrayList;
import java.util.List;

public final class WiremockPactApi {
  private final WireMockPactConfig config;
  private final List<ServeEvent> serveEvents = new ArrayList<ServeEvent>();

  private WiremockPactApi(final WireMockPactConfig config) {
    this.config = config;
  }

  public static WiremockPactApi create(final WireMockPactConfig config) {
    return new WiremockPactApi(config);
  }

  /** Record all given Wiremock requests to PACT. */
  public void toPact(final ServeEvent serveEvent) {
    this.serveEvents.add(serveEvent);
    this.saveAll();
  }

  private void saveAll() {
    final Consumer consumer = new Consumer(this.config.getConsumerDefaultValue());
    final Provider provider = new Provider(this.config.getProviderDefaultValue());
    final V4Pact v4 = new V4Pact(consumer, provider);
    for (final ServeEvent serveEvent : this.serveEvents) {
      final LoggedRequest wiremockRequest = serveEvent.getRequest();
      final Interaction interaction = new SynchronousHttp("generated from Wiremock");
      final SynchronousRequestResponse asSynchronousRequestResponse =
          interaction.asSynchronousRequestResponse();
      final IRequest request = asSynchronousRequestResponse.getRequest();
      request.setMethod(wiremockRequest.getMethod().getName());
      request.setPath(wiremockRequest.getUrl());
      request.setBody(new OptionalBody(State.PRESENT, wiremockRequest.getBody()));
      v4.getInteractions().add(interaction);
    }
    v4.write(this.config.getPactJsonFolder(), PactSpecVersion.V4);
  }
}
