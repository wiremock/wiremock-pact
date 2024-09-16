package se.bjurr.wiremockpact.wiremockpact.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class QueryParamsTest extends BaseTest {

  @BeforeEach
  public void beforeEach() {
    WireMock.reset();
    WireMock.stubFor(WireMock.any(WireMock.anyUrl()).willReturn(WireMock.ok()));
  }

  @Test
  public void testThatSingleHeaderIsRecorded() {
    RestAssured.given()
        .queryParam("foo", "bar")
        .log()
        .all()
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
              "description": "PUT /therequest?foo=bar -> 200",
              "key": "a08c5440",
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
                  ]
                },
                "method": "PUT",
                "path": "/therequest",
                "query": {
                  "foo": [
                    "bar"
                  ]
                }
              },
              "response": {
                "body": {
                  "content": ""
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
