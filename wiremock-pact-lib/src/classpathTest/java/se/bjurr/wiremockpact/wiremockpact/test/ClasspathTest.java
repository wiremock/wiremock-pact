package se.bjurr.wiremockpact.wiremockpact.test;

import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.wiremockpact.wiremockpact.test.RestClient.doRestCallGet;
import static se.bjurr.wiremockpact.wiremockpact.test.RestClient.doRestCallPost;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

public class ClasspathTest {

  private static WireMockServer server;
  private static WireMockPactApi wireMockPactApi;
  private static String BASE_URL;

  @BeforeAll
  public static void beforeEach() throws IOException {
    server = new WireMockServer();
    server.start();

    stubFor(
        get(anyUrl())
            .willReturn(
                ok().withHeader("content-type", "application/json")
                    .withBody("""
				{"a":"b"}
				""")));

    stubFor(
        post(anyUrl())
            .willReturn(
                ok().withHeader("content-type", "application/xml")
                    .withBody("""
				<nada></nada>
				""")));

    ClasspathTest.BASE_URL = "http://localhost:" + server.port();
    final Path tmpdir = Files.createTempDirectory(ClasspathTest.class.getName() + "-tempfile");
    wireMockPactApi =
        WireMockPactApi.create(
            new WireMockPactConfig()
                .setConsumerDefaultValue("my-service")
                .setProviderDefaultValue("unknown-service")
                .setProviderStatesDefaultValue(Arrays.asList("default"))
                .setPactJsonFolder(tmpdir.toFile().getAbsolutePath()));
    wireMockPactApi.clearAllSaved();
  }

  @Test
  public void testInvoke() throws Exception {
    doRestCallGet(BASE_URL + "/getrequest", "application/json");
    doRestCallPost(BASE_URL + "/postrequest", "application/json", """
				{"key":"value"}
				""");
    doRestCallPost(
        BASE_URL + "/postrequest", "application/xml", """
				<main><other-tag/></main>
				""");
  }

  @AfterAll
  public static void after() {
    for (final ServeEvent serveEvent : server.getAllServeEvents()) {
      wireMockPactApi.addServeEvent(serveEvent);
    }
    wireMockPactApi.saveAll();
    server.stop();

    final String savedJson =
        wireMockPactApi.getAllSavedContent().entrySet().stream()
            .map(e -> e.getKey() + ":\n" + e.getValue() + "\n")
            .collect(Collectors.joining("\n"));

    assertThat(savedJson)
        .isEqualToIgnoringNewLines(
            """
my-service-unknown-service.json:
{
  "consumer": {
    "name": "my-service"
  },
  "interactions": [
    {
      "description": "GET /getrequest -> 200",
      "key": "b36af786",
      "pending": false,
      "providerStates": [
        {
          "name": "default"
        }
      ],
      "request": {
        "body": {
          "content": ""
        },
        "headers": {
          "Accept": [
            "application/json"
          ]
        },
        "method": "GET",
        "path": "/getrequest"
      },
      "response": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "headers": {
          "content-type": [
            "application/json"
          ]
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /postrequest -> 200",
      "key": "bf7a2010",
      "pending": false,
      "providerStates": [
        {
          "name": "default"
        }
      ],
      "request": {
        "body": {
          "content": "<main><other-tag/></main>\\n",
          "contentType": "application/xml",
          "contentTypeHint": "DEFAULT",
          "encoded": false
        },
        "headers": {
          "Accept": [
            "application/xml"
          ],
          "Content-Length": [
            "26"
          ],
          "Content-Type": [
            "application/xml"
          ]
        },
        "method": "POST",
        "path": "/postrequest"
      },
      "response": {
        "body": {
          "content": "<nada></nada>\\n",
          "contentType": "application/xml",
          "contentTypeHint": "DEFAULT",
          "encoded": false
        },
        "headers": {
          "content-type": [
            "application/xml"
          ]
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /postrequest -> 200",
      "key": "bf7a2010",
      "pending": false,
      "providerStates": [
        {
          "name": "default"
        }
      ],
      "request": {
        "body": {
          "content": {
            "key": "value"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "headers": {
          "Accept": [
            "application/json"
          ],
          "Content-Length": [
            "16"
          ],
          "Content-Type": [
            "application/json"
          ]
        },
        "method": "POST",
        "path": "/postrequest"
      },
      "response": {
        "body": {
          "content": "<nada></nada>\\n",
          "contentType": "application/xml",
          "contentTypeHint": "DEFAULT",
          "encoded": false
        },
        "headers": {
          "content-type": [
            "application/xml"
          ]
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    }
  ],
  "metadata": {
    "pact-jvm": {
      "version": "4.6.9"
    },
    "pactSpecification": {
      "version": "4.0"
    }
  },
  "provider": {
    "name": "unknown-service"
  }
}

""");
  }
}
