package se.bjurr.wiremockpact.wiremockpact.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;
import se.bjurr.wiremockpact.wiremockpactlib.api.WiremockPactApi;

public class ExampleUsageTest extends BaseTest {

  @Test
  public void testThatGetRequestCanGeneratePact() throws Exception {
    // Setup Wiremock
    WireMock.stubFor(WireMock.get("/getrequest").willReturn(WireMock.ok()));

    // Invoke your code and let it invoke Wiremock
    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest")
        .then()
        .statusCode(200);

    // Use this library to create PACT json
    WiremockPactApi.create(
            WireMockPactConfig.builder()
                .withConsumerDefaultValue(this.me)
                .withProviderDefaultValue(this.you)
                .withPactJsonFolder(this.tmpdir.toString()))
        .toPact();

    final String pactContent = this.readPactFileContent(this.tmpdir, this.you, this.me);

    // And the content of that pact file will be
    assertThat(pactContent)
        .isEqualToIgnoringWhitespace(
            """
{
  "consumer": {
    "name": "this-is-me"
  },
  "interactions": [
    {
      "description": "generated from Wiremock",
      "key": "2b57c74e",
      "pending": false,
      "request": {
        "body": {
          "content": ""
        },
        "method": "GET",
        "path": "/getrequest"
      },
      "response": {
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
    "name": "this-is-you"
  }
}
""");
  }
}
