package se.bjurr.wiremockpact.wiremockpact.testutils;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import io.restassured.RestAssured;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi;

@WireMockTest
public class BaseTest {

  protected Path tmpdir;
  protected String you;
  protected String me;

  @BeforeEach
  public void baseBeforeEach(final WireMockRuntimeInfo wmRuntimeInfo) {
    RestAssured.baseURI = "http://localhost:" + wmRuntimeInfo.getHttpPort();
    this.tmpdir = this.getTempDir();
    this.you = "this-is-you";
    this.me = "this-is-me";
  }

  public Path getTempDir() {
    try {
      return Files.createTempDirectory(this.getClass().getName() + "-tempfile");
    } catch (final IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public String readPactFileContent(final Path tmpdir, final String you, final String me)
      throws IOException {
    final Path pactFile = tmpdir.resolve(me + "-" + you + ".json");
    final String pactContent = Files.readString(pactFile);
    return pactContent;
  }

  public void assertPactEquals(final String expected) {
    try {
      final Path tmpdir = this.getTempDir();
      final String you = "this-is-you";
      final String me = "this-is-me";
      final WireMockPactApi api =
          WireMockPactApi.create(
              WireMockPactConfig.builder()
                  .setConsumerDefaultValue(me)
                  .setProviderDefaultValue(you)
                  .setPactJsonFolder(tmpdir.toString()));
      api.clearAllSaved();
      for (final ServeEvent serveEvent : WireMock.getAllServeEvents()) {
        api.addServeEvent(serveEvent);
      }
      api.saveAll();
      final String pactContent = this.readPactFileContent(tmpdir, you, me);
      assertThat(pactContent).isEqualToIgnoringWhitespace(expected);
    } catch (final IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
