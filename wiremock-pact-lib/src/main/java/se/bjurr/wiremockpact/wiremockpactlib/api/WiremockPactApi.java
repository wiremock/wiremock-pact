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
import java.util.Objects;

public final class WiremockPactApi {
  private String consumerName = "consumer-name";
  private String providerName = "provider-name";

  private WiremockPactApi() {}

  public static WiremockPactApi builder() {
    return new WiremockPactApi();
  }

  public WiremockPactApi consumerName(final String consumerName) {
    this.consumerName = consumerName;
    return this;
  }

  public WiremockPactApi providerName(final String providerName) {
    this.providerName = providerName;
    return this;
  }

  /** Record all Wiremock requests to PACT. */
  public void toPact(final String pactDir) {
    final List<LoggedRequest> wiremockRequests =
        WireMock.findAll(WireMock.anyRequestedFor(WireMock.anyUrl()));
    this.toPact(wiremockRequests, pactDir);
  }

  /** Record all given Wiremock requests to PACT. */
  public void toPact(final List<LoggedRequest> wiremockRequests, final String pactDir) {
    Objects.requireNonNull(wiremockRequests);
    final Consumer consumer = new Consumer(this.consumerName);
    final Provider provider = new Provider(this.providerName);
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
    v4.write(pactDir, PactSpecVersion.V4);
  }
}
