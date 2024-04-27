# WireMock Pact

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/se.bjurr.wiremockpact/wiremock-pact/badge.svg)](https://repo1.maven.org/maven2/se/bjurr/wiremockpact)

It will get the requests from [WireMock](https://github.com/wiremock/wiremock/) and create [Pact JSON](https://docs.pact.io/) files on the filesystem. The Pact JSON can be published to a [Pactflow broker](https://test.pactflow.io/) with `curl`, see example in this readme.

This repostory contains:

- `wiremock-pact-lib` - *A library that can transform WireMock [ServeEvent](https://github.com/wiremock/wiremock/blob/master/src/main/java/com/github/tomakehurst/wiremock/stubbing/ServeEvent.java):s to Pact JSON.*
- `wiremock-pact-extension` - *A WireMock extension that is intended to ease usage of the library.*
- `wiremock-pact-example-springboot-app` - *A SpringBoot application that shows how it can be used.*

## Usage - Junit 5

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

## Usage - Library

It can be used as a library.

```java
public class ExampleTest {
  private static WireMockServer server;
  private static WireMockPactApi wireMockPactApi;

  @BeforeAll
  public static void beforeEach() throws IOException {
    server = new WireMockServer();
    server.start();

    stubFor(
        post(anyUrl())
            .willReturn(
                ok()
                .withHeader("content-type", "application/json")
                .withBody("""
                {"a":"b"}
                """))
            .withMetadata(
                new Metadata(
                    Map.of(
                        WireMockPactMetadata.METADATA_ATTR,
                        new WireMockPactMetadata()
                            .setProvider("some-specific-provider")))));

    wireMockPactApi =
        WireMockPactApi.create(
            new WireMockPactConfig()
                .setConsumerDefaultValue("my-service")
                .setProviderDefaultValue("unknown-service")
                .setPactJsonFolder("the/pact-json/folder"));
    wireMockPactApi.clearAllSaved();
  }

  @Test
  public void testInvoke() {
    // Do stuff that invokes WireMock...
  }

  @AfterAll
  public static void after() {
    for (final ServeEvent serveEvent : server.getAllServeEvents()) {
      wireMockPactApi.addServeEvent(serveEvent);
    }
    // Save pact-json to folder given in WireMockPactApi
    wireMockPactApi.saveAll();
    server.stop();
  }
}
```

## Mappings metadata - Set provider in mapping

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

Or programmatically:

```java
    stubFor(
        post(anyUrl())
            .withMetadata(
                new Metadata(
                    Map.of(
                        WireMockPactMetadata.METADATA_ATTR,
                        new WireMockPactMetadata()
                            .setProvider("some-specific-provider")))));
```

## Publishing to Pact broker

Pact has a [CLI tool](https://docs.pact.io/pact_broker/publishing_and_retrieving_pacts) that can be used for publishing the contracts. But it requires Ruby or Docker. If you don't have that, perhaps `curl` is an option.

You may want to use something like [git-changelog-command-line](https://github.com/tomasbjerre/git-changelog-command-line) to get the next version.

There is a test-server at https://test.pactflow.io/ that can be accessed with user `dXfltyFMgNOFZAxr8io9wJ37iUpY42M` and password `O5AIZWxelWbLvqMd8PkAVycBJh2Psyg1`.

```sh
pact_user=dXfltyFMgNOFZAxr8io9wJ37iUpY42M
pact_password=O5AIZWxelWbLvqMd8PkAVycBJh2Psyg1 # This is not a mistake, these credentials are publicly available
pact_broker_url=https://test.pactflow.io/contracts/publish
build_url=https://ci/builds/1234
pact_json_folder="wiremock-pact-example-springboot-app/src/test/resources/pact-json/*.json"
for pact_json_file in $pact_json_folder
do
  echo "Processing $pact_json_file file..."
  json=`cat $pact_json_file`
  current_version=$(npx git-changelog-command-line \
  --patch-version-pattern "^fix.*" \
  --print-current-version)
  git_hash=`git rev-parse --short HEAD`
  pacticipant_version_number="$current_version-$git_hash"
  consumer_name=`echo $json | jq -r '.consumer.name'`
  provider_name=`echo $json | jq -r '.provider.name'`
  content_base64=`echo $json | base64`
  content_base64=`echo $content_base64 | sed 's/[[:space:]]//g'`
  branch=$(git symbolic-ref --short HEAD)

  read -r -d '' publish_content << EndOfMessage
{
  "pacticipantName": "$consumer_name",
  "pacticipantVersionNumber": "$pacticipant_version_number",
  "branch": "$branch",
  "tags": [
    "$branch"
  ],
  "buildUrl": "$build_url",
  "contracts": [
    {
      "consumerName": "$consumer_name",
      "providerName": "$provider_name",
      "specification": "pact",
      "contentType": "application/json",
      "content": "$content_base64"
    }
  ]
}
EndOfMessage

  echo Publishing:
  echo
  echo $publish_content
  echo
  publish_content_file=$(mktemp)
  echo $publish_content > $publish_content_file

  curl -v -X POST \
    -u "$pact_user:$pact_password" \
    $pact_broker_url \
    -H "Content-Type: application/json" \
    --data-binary @$publish_content_file
done
```
