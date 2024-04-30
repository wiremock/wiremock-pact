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
      "key": "6e220e6f1cd4f9dc289b991992e15f26563fe5519d38c4034eec9bc90056fef3",
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
    "pactSpecification": {
      "version": "4.0"
    },
    "tool": "se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi"
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
      "key": "5be03c94b3c34d9fa7f614a492938c1feefb745d52ab434cf26b3beafe2f4e1a",
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
      "key": "4d51e23bd829d5fee69270b95fe737a6f37ed05e92c710e549f05115db94756e",
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
      "key": "fa678c53c75afb8c2a0c312c01d6c1ae3cd924d452ce98968593d2f9e66fd5c6",
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
    "pactSpecification": {
      "version": "4.0"
    },
    "tool": "se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi"
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
      "key": "0149dcc32683cf78a7fb5b2b8f5e450d8bbe9b5b64a5106b4d787355284a7adf",
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
        "path": "/getrequest_1?a=b"
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
      "key": "d7a637d4099328bf71d295b75c5e03499aec7b8993d953e9b37384e0e9d1cc8b",
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
        "path": "/getrequest_2?a=b&a=b"
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
      "key": "74c66408e23aa7e0f5c02a3766b1730888a7a3e62058fc7effbc8c5001edb54e",
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
        "path": "/getrequest_3?a=b&c=d&e=f"
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
    "pactSpecification": {
      "version": "4.0"
    },
    "tool": "se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi"
  },
  "provider": {
    "name": "this-is-you"
  }
}
""");
  }
}
