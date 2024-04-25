package se.bjurr.wiremockpact.wiremockpact.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class WiremockRequestTest extends BaseTest {

  @Test
  public void testThatGetRequestCanGeneratePact() {
    WireMock.stubFor(WireMock.get("/getrequest").willReturn(WireMock.ok()));

    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest")
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
