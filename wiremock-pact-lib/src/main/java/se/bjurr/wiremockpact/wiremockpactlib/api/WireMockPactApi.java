package se.bjurr.wiremockpact.wiremockpactlib.api;

import au.com.dius.pact.core.model.Consumer;
import au.com.dius.pact.core.model.IRequest;
import au.com.dius.pact.core.model.IResponse;
import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.OptionalBody;
import au.com.dius.pact.core.model.OptionalBody.State;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.Provider;
import au.com.dius.pact.core.model.SynchronousRequestResponse;
import au.com.dius.pact.core.model.V4Interaction.SynchronousHttp;
import au.com.dius.pact.core.model.V4Pact;
import com.github.tomakehurst.wiremock.common.Metadata;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.LoggedResponse;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;
import se.bjurr.wiremockpact.wiremockpactlib.api.model.MetadataModelWireMockPactSettings;
import se.bjurr.wiremockpact.wiremockpactlib.internal.JsonHelper;

public final class WireMockPactApi {
  private static final Logger LOG = Logger.getLogger(WireMockPactApi.class.getSimpleName());
  private final WireMockPactConfig config;
  private final List<ServeEvent> serveEvents = new ArrayList<>();

  private WireMockPactApi(final WireMockPactConfig config) {
    this.config = config;
  }

  public static WireMockPactApi create(final WireMockPactConfig config) {
    return new WireMockPactApi(config);
  }

  public void addServeEvent(final ServeEvent serveEvent) {
    this.serveEvents.add(serveEvent);
  }

  /** Record all given Wiremock requests to PACT. */
  public void saveAll() {
    LOG.info(
        "Saving " + this.serveEvents.size() + " serveevents to " + this.getAbsoluteJsonFolder());
    final Consumer consumer = new Consumer(this.config.getConsumerDefaultValue());
    final String defaultProvider = this.config.getProviderDefaultValue();
    final Map<String, List<Interaction>> interactionsPerProvider = new TreeMap<>();
    for (final ServeEvent serveEvent : this.serveEvents) {
      final MetadataModelWireMockPactSettings pactSettings = this.getMetadataModel(serveEvent);
      final String provider =
          Optional.ofNullable(pactSettings.getProvider()).orElse(defaultProvider);

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
      for (final HttpHeader header : request.getHeaders().all()) {
        if (header.getKey().matches(this.config.getIncludeRequestHeadersRegexp())) {
          pactRequest.getHeaders().put(header.getKey(), header.getValues());
        }
      }
      if (!interactionsPerProvider.containsKey(provider)) {
        interactionsPerProvider.put(provider, new ArrayList<>());
      }

      final IResponse pactResponse = asSynchronousRequestResponse.getResponse();
      pactResponse.setBody(new OptionalBody(State.PRESENT, response.getBody()));
      pactResponse.setStatus(response.getStatus());
      for (final HttpHeader header : response.getHeaders().all()) {
        if (header.getKey().matches(this.config.getIncludeResponseHeadersRegexp())) {
          pactResponse.getHeaders().put(header.getKey(), header.getValues());
        }
      }

      interactionsPerProvider.get(provider).add(interaction);
    }

    for (final Entry<String, List<Interaction>> provider : interactionsPerProvider.entrySet()) {
      final V4Pact v4 = new V4Pact(consumer, new Provider(provider.getKey()));
      v4.getInteractions().addAll(provider.getValue());
      LOG.fine("Saving " + v4 + " to " + this.getAbsoluteJsonFolder());
      v4.write(this.getAbsoluteJsonFolder(), PactSpecVersion.V4);
    }
  }

  private MetadataModelWireMockPactSettings getMetadataModel(final ServeEvent serveEvent) {
    final Metadata metadata = serveEvent.getStubMapping().getMetadata();
    if (metadata == null) {
      return new MetadataModelWireMockPactSettings();
    }
    final String json = JsonHelper.toJson(metadata.get("wireMockPactSettings"));
    return JsonHelper.fromJson(MetadataModelWireMockPactSettings.class, json);
  }

  private String getAbsoluteJsonFolder() {
    return Paths.get(this.config.getPactJsonFolder()).toFile().getAbsolutePath();
  }

  public void clearAllSaved() {
    final String absoluteJsonFolder = this.getAbsoluteJsonFolder();
    LOG.info("Clearing PACT JSON in " + absoluteJsonFolder);
    final Path path = Paths.get(absoluteJsonFolder);
    if (path == null || path.toFile() == null || path.toFile().listFiles() == null) {
      return;
    }
    Arrays.asList(path.toFile().listFiles())
        .forEach(
            it -> {
              if (it.getName().endsWith(".json")) {
                if (!it.delete()) {
                  LOG.warning("Unable to delete " + it.getAbsolutePath());
                }
              }
            });
  }
}
