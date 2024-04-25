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
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.util.List;

public final class WiremockPactApi {
  private final WireMockPactConfig config;

  private WiremockPactApi(final WireMockPactConfig config) {
    this.config = config;
  }

  public static WiremockPactApi create(final WireMockPactConfig config) {
    return new WiremockPactApi(config);
  }

  /** Record all Wiremock requests to PACT. */
  public void toPact() {
    final List<LoggedRequest> wiremockRequests =
        WireMock.findAll(WireMock.anyRequestedFor(WireMock.anyUrl()));
    this.toPact(wiremockRequests);
  }

  /** Record all given Wiremock requests to PACT. */
  public void toPact(final List<LoggedRequest> wiremockRequests) {
    final Consumer consumer = new Consumer(this.config.getConsumerDefaultValue());
    final Provider provider = new Provider(this.config.getProviderDefaultValue());
    final V4Pact v4 = new V4Pact(consumer, provider);
    for (final LoggedRequest wiremockRequest : wiremockRequests) {
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
