package se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:autotest.properties")
@EnableWireMock({
  @ConfigureWireMock(
      name = "wiremock-service-name",
      property = "wiremock.server.url",
      stubLocation = "wiremock",
      configurationCustomizers = {WireMockConfig.class})
})
public class TestBase {

  @Autowired public MockMvc mockMvc;

  public MvcResult getAnimals() throws Exception {
    return this.mockMvc.perform(get("/animals")).andDo(log()).andReturn();
  }

  public MvcResult getAnimal(final String id) throws Exception {
    return this.mockMvc.perform(get("/animals/" + id)).andDo(log()).andReturn();
  }
}
