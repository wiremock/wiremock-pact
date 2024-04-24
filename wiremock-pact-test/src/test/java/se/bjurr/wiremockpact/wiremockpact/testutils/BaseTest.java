package se.bjurr.wiremockpact.wiremockpact.testutils;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import se.bjurr.wiremockpact.wiremockpactlib.api.WiremockPactApi;

@WireMockTest
public class BaseTest {

  @BeforeEach
  public void before(final WireMockRuntimeInfo wmRuntimeInfo) {
    RestAssured.baseURI = "http://localhost:" + wmRuntimeInfo.getHttpPort();
  }

  public void assertPactEquals(final String expected) {
    Path tmpdir = null;
    Path pactFile = null;
    try {
      tmpdir = Files.createTempDirectory(this.getClass().getName() + "-tempfile");
      final String you = "this-is-you";
      final String me = "this-is-me";
      WiremockPactApi.builder().consumerName(me).providerName(you).toPact(tmpdir.toString());
      pactFile = tmpdir.resolve(me + "-" + you + ".json");
      final String pactContent = Files.readString(pactFile);
      assertThat(pactContent).isEqualToIgnoringWhitespace(expected);
    } catch (final IOException e) {
      throw new RuntimeException(pactFile.toString(), e);
    }
  }
}
