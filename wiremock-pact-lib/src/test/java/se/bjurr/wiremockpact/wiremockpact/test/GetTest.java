package se.bjurr.wiremockpact.wiremockpact.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class GetTest extends BaseTest {

  @BeforeEach
  public void beforeEach() {
    WireMock.reset();
    WireMock.stubFor(WireMock.any(WireMock.anyUrl()).willReturn(WireMock.ok()));
  }

  @Test
  public void testThatSingleGetRequestCanGeneratePact() {
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
          "content": ""
        },
        "status": 200
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

  @Test
  public void testThatSingleGetRequestCanGeneratePact_3_invocations() {
    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest_1")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest_2")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest_3")
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
      "description": "GET /getrequest_1 -> 200",
      "key": "5dd940a3",
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
        "path": "/getrequest_1"
      },
      "response": {
        "body": {
          "content": ""
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "GET /getrequest_2 -> 200",
      "key": "d4862e38",
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
        "path": "/getrequest_2"
      },
      "response": {
        "body": {
          "content": ""
        },
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "GET /getrequest_3 -> 200",
      "key": "4b331bcd",
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
        "path": "/getrequest_3"
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

  @Test
  public void testThatSingleGetRequestCanGeneratePact_3_invocations_query() {
    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest_1?a=b")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest_2?a=b&a=b")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .accept(ContentType.JSON)
        .when()
        .get("/getrequest_3?a=b&c=d&e=f")
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
      "description": "GET /getrequest_1?a=b -> 200",
      "key": "259d0ff6",
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
        "path": "/getrequest_1",
        "query": {
          "a": [
            "b"
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
    },
    {
      "description": "GET /getrequest_2?a=b&a=b -> 200",
      "key": "7d0b810b",
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
        "path": "/getrequest_2",
        "query": {
          "a": [
            "b",
            "b"
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
    },
    {
      "description": "GET /getrequest_3?a=b&c=d&e=f -> 200",
      "key": "d357be9c",
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
        "path": "/getrequest_3",
        "query": {
          "a": [
            "b"
          ],
          "c": [
            "d"
          ],
          "e": [
            "f"
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
