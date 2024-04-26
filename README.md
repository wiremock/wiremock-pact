# WireMock PACT

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/se.bjurr.wiremockpact/wiremock-pact/badge.svg)](https://repo1.maven.org/maven2/se/bjurr/wiremockpact)

Get requests from [WireMock](https://github.com/wiremock/wiremock/) and create [PACT JSON](https://docs.pact.io/). You may publish the JSON to a broker using [pact-jvm](https://github.com/pact-foundation/pact-jvm/tree/master/provider).

This repostory contains:

- `wiremock-pact-lib` - *A library that can transform WireMock [ServeEvent](https://github.com/wiremock/wiremock/blob/master/src/main/java/com/github/tomakehurst/wiremock/stubbing/ServeEvent.java):s to PACT JSON.*
- `wiremock-pact-extension` - *A WireMock extension that is intended to ease usage of the library.*
- `wiremock-pact-example-springboot-app` - *A SpringBoot application that shows how it can be used.*

## Usage with Junit 5

The extension is both a WireMock extension and a JUnit 5 extension. When using [`wiremock-spring-boot`](https://wiremock.org/docs/solutions/spring-boot/) it can be configured like this in a base class of your tests:

```java
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.WireMockConfigurationCustomizer;
import org.junit.jupiter.api.extension.RegisterExtension;
import se.bjurr.wiremockpact.wiremockpactextension.WireMockPactExtension;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

@EnableWireMock({
  @ConfigureWireMock(
      name = "wiremock-service-name",
      property = "wiremock.server.url",
      stubLocation = "wiremock",
      configurationCustomizers = {WireMockPactBaseTest.class})
})
public class WireMockPactBaseTest implements WireMockConfigurationCustomizer {
  @RegisterExtension
  static WireMockPactExtension WIREMOCK_PACT_EXTENSION =
      new WireMockPactExtension(
          WireMockPactConfig.builder() //
              .setConsumerDefaultValue("WireMockPactExample") //
              .setProviderDefaultValue("UnknownProvider") //
              .setPactJsonFolder("src/test/resources/pact-json"));

  @Override
  public void customize(
      final WireMockConfiguration configuration, final ConfigureWireMock options) {
    configuration.extensions(WIREMOCK_PACT_EXTENSION);
  }
}
```

## Set provider of a mapping

You can adjust any mappings file like this to specify the provider of a mapping in its [metadata](https://github.com/wiremock/spec/blob/main/wiremock/wiremock-admin-api/schemas/stub-mapping.yaml) field:

```diff
{
  "id" : "d68fb4e2-48ed-40d2-bc73-0a18f54f3ece",
  "request" : {
    "urlPattern" : "/animals/1",
    "method" : "GET"
  },
  "response" : {
    "status" : 202
  },
  "uuid" : "d68fb4e2-48ed-40d2-bc73-0a18f54f3ece",
+  "metadata": {
+   "wireMockPactSettings": {
+     "provider":"some-other-system"
+   }
+  }
}
```
