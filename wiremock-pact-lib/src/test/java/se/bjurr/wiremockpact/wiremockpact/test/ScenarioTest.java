package se.bjurr.wiremockpact.wiremockpact.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class ScenarioTest extends BaseTest {

  @BeforeEach
  public void beforeEach() {
    WireMock.reset();

    stubFor(
        get(WireMock.urlEqualTo("/todo/items"))
            .inScenario("To do list")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withBody("<items>" + "   <item>Buy milk</item>" + "</items>")));

    stubFor(
        post(urlEqualTo("/todo/items"))
            .inScenario("To do list")
            .whenScenarioStateIs(STARTED)
            .withRequestBody(containing("Cancel newspaper subscription"))
            .willReturn(aResponse().withStatus(201))
            .willSetStateTo("Cancel newspaper item added"));

    stubFor(
        get(urlEqualTo("/todo/items"))
            .inScenario("To do list")
            .whenScenarioStateIs("Cancel newspaper item added")
            .willReturn(
                aResponse()
                    .withBody(
                        "<items>"
                            + "   <item>Buy milk</item>"
                            + "   <item>Cancel newspaper subscription</item>"
                            + "</items>")));
  }

  @Test
  public void testThatProviderStateIsSetFromScenario() {
    RestAssured.given().log().all().when().get("/todo/items").then().statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .given()
        .body("Cancel newspaper subscription")
        .when()
        .post("/todo/items")
        .then()
        .statusCode(201);

    RestAssured.given().log().all().when().get("/todo/items").then().statusCode(200);

    this.assertPactEquals(
        """
{
  "consumer": {
    "name": "this-is-me"
  },
  "interactions": [
    {
      "description": "GET /todo/items -> 200",
      "key": "62b2f31c",
      "pending": false,
      "providerStates": [
        {
          "name": "To do list---Cancel newspaper item added"
        }
      ],
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
          ]
        },
        "method": "GET",
        "path": "/todo/items"
      },
      "response": {
        "body": {
          "content": "<items>   <item>Buy milk</item>   <item>Cancel newspaper subscription</item></items>",
          "contentType": "application/xml",
          "contentTypeHint": "DEFAULT",
          "encoded": false
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "GET /todo/items -> 200",
      "key": "65cf9bbb",
      "pending": false,
      "providerStates": [
        {
          "name": "To do list---Started"
        }
      ],
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
          ]
        },
        "method": "GET",
        "path": "/todo/items"
      },
      "response": {
        "body": {
          "content": "<items>   <item>Buy milk</item></items>",
          "contentType": "application/xml",
          "contentTypeHint": "DEFAULT",
          "encoded": false
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /todo/items -> 201",
      "key": "424a4178",
      "pending": false,
      "providerStates": [
        {
          "name": "To do list---Started"
        }
      ],
      "request": {
        "body": {
          "content": "Cancel newspaper subscription",
          "contentType": "text/plain",
          "contentTypeHint": "DEFAULT",
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
            "29"
          ],
          "Content-Type": [
            "text/plain; charset=ISO-8859-1"
          ]
        },
        "method": "POST",
        "path": "/todo/items"
      },
      "response": {
        "body": {
          "content": ""
        },
        "status": 201
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
