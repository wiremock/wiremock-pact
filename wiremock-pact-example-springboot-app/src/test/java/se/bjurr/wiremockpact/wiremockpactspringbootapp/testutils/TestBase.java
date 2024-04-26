package se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.WireMockConfigurationCustomizer;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import se.bjurr.wiremockpact.wiremockpactextension.WireMockPactExtension;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:autotest.properties")
@EnableWireMock({
  @ConfigureWireMock(
      name = "wiremock-service-name",
      property = "wiremock.server.url",
      stubLocation = "wiremock",
      configurationCustomizers = {TestBase.class})
})
public class TestBase implements WireMockConfigurationCustomizer {
  @RegisterExtension
  static WireMockPactExtension WIREMOCK_PACT_EXTENSION =
      new WireMockPactExtension(WireMockPactConfig.builder().setPactJsonFolder("whatevertomas"));

  @Override
  public void customize(
      final WireMockConfiguration configuration, final ConfigureWireMock options) {
    configuration.extensions(WIREMOCK_PACT_EXTENSION);
  }

  @Autowired public MockMvc mockMvc;

  public MvcResult getAnimals() throws Exception {
    return this.mockMvc.perform(get("/animals")).andDo(log()).andReturn();
  }

  public MvcResult getAnimal(final String id) throws Exception {
    return this.mockMvc.perform(get("/animals/" + id)).andDo(log()).andReturn();
  }
}
