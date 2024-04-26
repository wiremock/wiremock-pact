package se.bjurr.wiremockpact.wiremockpact.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class HeadersTest extends BaseTest {

  @BeforeEach
  public void beforeEach() {
    WireMock.reset();
    WireMock.stubFor(
        WireMock.any(WireMock.anyUrl())
            .willReturn(WireMock.ok().withHeader("response-header", "response-value")));
  }

  @Test
  public void testThatSingleHeaderIsRecorded() {
    RestAssured.given()
        .log()
        .all()
        .header("singleheader", "singlevalue")
        .when()
        .put("/therequest")
        .then()
        .statusCode(200);

    this.assertPactEquals(
        """
{
  "consumer": {
    "name": "this-is-me"
  },
  "interactions": [
    {
      "description": "PUT /therequest -> 200",
      "key": "74291a1f",
      "pending": false,
      "request": {
        "body": {
          "content": ""
        },
        "headers": {
          "Accept": [
            "*/*"
          ],
          "Accept-Encoding": [
            "gzip,deflate"
          ],
          "Content-Length": [
            "0"
          ],
          "singleheader": [
            "singlevalue"
          ]
        },
        "method": "PUT",
        "path": "/therequest"
      },
      "response": {
        "body": {
          "content": ""
        },
        "headers": {
          "response-header": [
            "response-value"
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
    "name": "this-is-you"
  }
}

""");
  }
}
