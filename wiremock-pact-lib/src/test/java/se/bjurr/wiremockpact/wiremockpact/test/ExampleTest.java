package se.bjurr.wiremockpact.wiremockpact.test;

import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi.WIRE_MOCK_METADATA_PACT_SETTINGS;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Metadata;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;
import se.bjurr.wiremockpact.wiremockpactlib.api.model.MetadataModelWireMockPactSettings;

public class ExampleTest {

  private static WireMockServer server;
  private static WireMockPactApi wireMockPactApi;

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
                ok().withHeader("content-type", "application/json")
                    .withBody("""
				{"a":"b"}
				"""))
            .withMetadata(
                new Metadata(
                    Map.of(
                        WIRE_MOCK_METADATA_PACT_SETTINGS,
                        new MetadataModelWireMockPactSettings("some-specific-provider")))));

    RestAssured.baseURI = "http://localhost:" + server.port();
    final Path tmpdir = Files.createTempDirectory(ExampleTest.class.getName() + "-tempfile");
    wireMockPactApi =
        WireMockPactApi.create(
            new WireMockPactConfig()
                .setConsumerDefaultValue("my-service")
                .setProviderDefaultValue("unknown-service")
                .setPactJsonFolder(tmpdir.toFile().getAbsolutePath()));
    wireMockPactApi.clearAllSaved();
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
my-service-some-specific-provider.json:
{
  "consumer": {
    "name": "my-service"
  },
  "interactions": [
    {
      "description": "POST /postrequest -> 200",
      "key": "99132e13",
      "pending": false,
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
            "*/*"
          ],
          "Accept-Encoding": [
            "gzip,deflate"
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
    "name": "some-specific-provider"
  }
}


my-service-unknown-service.json:
{
  "consumer": {
    "name": "my-service"
  },
  "interactions": [
    {
      "description": "GET /getrequest -> 200",
      "key": "8d040589",
      "pending": false,
      "request": {
        "body": {
          "content": ""
        },
        "headers": {
          "Accept": [
            "application/json, application/javascript, text/javascript, text/json"
          ],
          "Accept-Encoding": [
            "gzip,deflate"
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

  @Test
  public void testInvoke() {
    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
				{"key":"value"}
				""")
        .when()
        .post("/postrequest")
        .then()
        .statusCode(200);
  }
}
