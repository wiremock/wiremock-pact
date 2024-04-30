package se.bjurr.wiremockpact.wiremockpactlib.api;

import static se.bjurr.wiremockpact.wiremockpactlib.api.model.WireMockPactMetadata.METADATA_ATTR;

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
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.Pact;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactBody;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactInteraction;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactMetadata;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactMetadataVersion;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactPacticipant;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactProviderState;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactRequest;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactResponse;
import se.bjurr.wiremockpact.wiremockpactlib.internal.JsonHelper;
import se.bjurr.wiremockpact.wiremockpactlib.internal.PactModelHelper;

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
    final String defaultProvider = this.config.getProviderDefaultValue();
    final List<String> defaultProviderStates = this.config.getProviderStatesDefaultValue();
    final Map<String, List<PactInteraction>> interactionsPerProvider = new TreeMap<>();
    for (final ServeEvent wireMockServeEvent : this.serveEvents) {
      final WireMockPactMetadata pactSettings = this.getMetadataModel(wireMockServeEvent);

      final String provider =
          Optional.ofNullable(pactSettings.getProvider()).orElse(defaultProvider);
      if (!interactionsPerProvider.containsKey(provider)) {
        interactionsPerProvider.put(provider, new ArrayList<>());
      }

      final List<String> providerStates = new ArrayList<>();
      if (pactSettings.getProviderStates().isEmpty()) {
        providerStates.addAll(defaultProviderStates);
      } else {
        providerStates.addAll(pactSettings.getProviderStates());
      }

      final LoggedRequest wireMockRequest = wireMockServeEvent.getRequest();
      final LoggedResponse wireMockResponse = wireMockServeEvent.getResponse();

      final String pactDescription =
          wireMockRequest.getMethod().getName()
              + " "
              + wireMockRequest.getUrl()
              + " -> "
              + wireMockResponse.getStatus();
      final List<PactProviderState> pactProviderStates =
          providerStates.stream().map(it -> new PactProviderState().setName(it)).toList();
      final PactInteraction pactInteraction =
          new PactInteraction()
              .setDescription(pactDescription)
              .setProviderStates(pactProviderStates);

      final PactBody pactRequestBody =
          this.getPactBody(
              wireMockRequest.getHeader("content-type"), wireMockRequest.getBodyAsString());
      final PactRequest pactRequest =
          new PactRequest()
              .setMethod(wireMockRequest.getMethod().getName())
              .setPath(wireMockRequest.getUrl())
              .setBody(pactRequestBody);
      final Map<String, List<String>> requestHeaders = new TreeMap<String, List<String>>();
      for (final HttpHeader header : wireMockRequest.getHeaders().all()) {
        if (header.getKey().matches(this.config.getIncludeRequestHeadersRegexp())) {
          requestHeaders.put(header.getKey(), header.getValues());
        }
      }
      pactRequest.setHeaders(requestHeaders);
      pactInteraction.setRequest(pactRequest);

      final PactBody pactResponseBody =
          new PactBody().setContent(wireMockResponse.getBodyAsString());
      final PactResponse pactResponse =
          new PactResponse().setBody(pactResponseBody).setStatus(wireMockResponse.getStatus());
      final Map<String, List<String>> responseHeaders = new TreeMap<String, List<String>>();
      for (final HttpHeader header : wireMockResponse.getHeaders().all()) {
        if (header.getKey().matches(this.config.getIncludeResponseHeadersRegexp())) {
          responseHeaders.put(header.getKey(), header.getValues());
        }
      }
      pactResponse.setHeaders(responseHeaders);
      pactInteraction
          .setResponse(pactResponse)
          .setKey(PactModelHelper.generateKey(pactInteraction));
      interactionsPerProvider.get(provider).add(pactInteraction);
    }

    for (final Entry<String, List<PactInteraction>> provider : interactionsPerProvider.entrySet()) {
      final List<PactInteraction> interactions = provider.getValue().stream().sorted().toList();
      final Pact pact =
          new Pact()
              .setConsumer(new PactPacticipant().setName(this.config.getConsumerDefaultValue()))
              .setProvider(new PactPacticipant().setName(provider.getKey()))
              .setInteractions(interactions)
              .setMetadata(
                  new PactMetadata()
                      .setTool(this.getClass().getName())
                      .setPactSpecification(new PactMetadataVersion().setVersion("4.0")));
      LOG.fine("Saving " + pact + " to " + this.getAbsoluteJsonFolder());
      PactModelHelper.writeToFile(pact, this.getAbsoluteJsonFolder());
    }
  }

  private PactBody getPactBody(final String mimeType, final String bodyAsString) {
    return new PactBody().setContent(bodyAsString).setContentType(mimeType);
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
