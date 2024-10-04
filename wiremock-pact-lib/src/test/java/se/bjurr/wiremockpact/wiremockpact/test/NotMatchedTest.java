package se.bjurr.wiremockpact.wiremockpact.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class NotMatchedTest extends BaseTest {

  @BeforeEach
  public void beforeEach() {
    WireMock.reset();
    WireMock.stubFor(WireMock.any(WireMock.urlEqualTo("whatever")).willReturn(WireMock.ok()));
  }

  @Test
  public void testRequestNotMatching() {
    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest")
        .then()
        .statusCode(404);

    this.assertPactEquals(
        """
{
  "consumer": {
    "name": "this-is-me"
  },
  "interactions": [
    {
      "description": "GET /getrequest -> 404",
      "key": "8d04584b",
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
          "content": ""
        },
        "status": 404
      },
      "type": "Synchronous/HTTP"
    }
  ],
  "metadata": {
    "pact-jvm": {
      "version": "4.6.14"
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
