package se.bjurr.wiremockpact.wiremockpactlib.api;

import static se.bjurr.wiremockpact.wiremockpactlib.api.model.WireMockPactMetadata.METADATA_ATTR;

import au.com.dius.pact.core.model.Consumer;
import au.com.dius.pact.core.model.IRequest;
import au.com.dius.pact.core.model.IResponse;
import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.OptionalBody;
import au.com.dius.pact.core.model.OptionalBody.State;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.Provider;
import au.com.dius.pact.core.model.ProviderState;
import au.com.dius.pact.core.model.SynchronousRequestResponse;
import au.com.dius.pact.core.model.V4Interaction.SynchronousHttp;
import au.com.dius.pact.core.model.V4Pact;
import com.github.tomakehurst.wiremock.common.Metadata;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.LoggedResponse;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import java.util.stream.Collectors;
import se.bjurr.wiremockpact.wiremockpactlib.api.model.WireMockPactMetadata;
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
    final List<String> defaultProviderStates = this.config.getProviderStatesDefaultValue();
    final Map<String, List<Interaction>> interactionsPerProvider = new TreeMap<>();
    for (final ServeEvent serveEvent : this.serveEvents) {
      final WireMockPactMetadata pactSettings = this.getMetadataModel(serveEvent);

      final String provider =
          Optional.ofNullable(pactSettings.getProvider()).orElse(defaultProvider);

      final List<String> providerStates = new ArrayList<>();
      if (pactSettings.getProviderStates().isEmpty()) {
        providerStates.addAll(defaultProviderStates);
      } else {
        providerStates.addAll(pactSettings.getProviderStates());
      }

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

      interaction
          .getProviderStates()
          .addAll(providerStates.stream().map(it -> new ProviderState(it)).toList());

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

  private WireMockPactMetadata getMetadataModel(final ServeEvent serveEvent) {
    final Metadata metadata = serveEvent.getStubMapping().getMetadata();
    if (metadata == null) {
      return new WireMockPactMetadata();
    }
    final String json = JsonHelper.toJson(metadata.get(METADATA_ATTR));
    return JsonHelper.fromJson(WireMockPactMetadata.class, json);
  }

  private String getAbsoluteJsonFolder() {
    return Paths.get(this.config.getPactJsonFolder()).toFile().getAbsolutePath();
  }

  public List<File> getAllSaved() {
    final String absoluteJsonFolder = this.getAbsoluteJsonFolder();
    LOG.info("Clearing PACT JSON in " + absoluteJsonFolder);
    final Path path = Paths.get(absoluteJsonFolder);
    if (path == null || path.toFile() == null || path.toFile().listFiles() == null) {
      return new ArrayList<File>();
    }
    return Arrays.asList(path.toFile().listFiles()).stream()
        .filter(it -> it.getName().endsWith(".json"))
        .map(it -> it)
        .toList();
  }

  public Map<String, String> getAllSavedContent() {
    return new TreeMap<>(
        this.getAllSaved().stream()
            .collect(
                Collectors.toMap(
                    File::getName,
                    it -> {
                      try {
                        return Files.readString(it.toPath());
                      } catch (final IOException e) {
                        throw new RuntimeException(e);
                      }
                    })));
  }

  public void clearAllSaved() {
    final String absoluteJsonFolder = this.getAbsoluteJsonFolder();
    LOG.info("Clearing PACT JSON in " + absoluteJsonFolder);
    final Path path = Paths.get(absoluteJsonFolder);
    if (path == null || path.toFile() == null || path.toFile().listFiles() == null) {
      return;
    }
    this.getAllSaved()
        .forEach(
            it -> {
              if (!it.delete()) {
                LOG.warning("Unable to delete " + it.getAbsolutePath());
              }
            });
  }
}
