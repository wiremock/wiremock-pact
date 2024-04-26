package se.bjurr.wiremockpact.wiremockpact.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.bjurr.wiremockpact.wiremockpact.testutils.BaseTest;

public class PostTest extends BaseTest {

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
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest")
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
      "description": "POST /postrequest -> 200",
      "key": "99132e13",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest"
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

  @Test
  public void testThatSingleGetRequestCanGeneratePact_3_invocations() {
    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest")
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
      "description": "POST /postrequest -> 200",
      "key": "99132e13",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest"
      },
      "response": {
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /postrequest -> 200",
      "key": "99132e13",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest"
      },
      "response": {
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /postrequest -> 200",
      "key": "99132e13",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest"
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

  @Test
  public void testThatSingleGetRequestCanGeneratePact_3_invocations_query() {
    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest?a=b")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest?a=b&a=b")
        .then()
        .statusCode(200);

    RestAssured.given()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body("""
        		{"a":"b"}
        		""")
        .when()
        .post("/postrequest?a=b&c=d&e=f")
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
      "description": "POST /postrequest?a=b -> 200",
      "key": "2e42c566",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest?a=b"
      },
      "response": {
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /postrequest?a=b&a=b -> 200",
      "key": "f6b475e6",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest?a=b&a=b"
      },
      "response": {
        "status": 200
      },
      "type": "Synchronous/HTTP"
    },
    {
      "description": "POST /postrequest?a=b&c=d&e=f -> 200",
      "key": "bf8097e2",
      "pending": false,
      "request": {
        "body": {
          "content": {
            "a": "b"
          },
          "contentType": "application/json",
          "encoded": false
        },
        "method": "POST",
        "path": "/postrequest?a=b&c=d&e=f"
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
