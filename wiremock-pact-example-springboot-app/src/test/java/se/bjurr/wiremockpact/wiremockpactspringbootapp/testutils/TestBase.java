package se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:autotest.properties")
public class TestBase extends WireMockPactBaseTest {

  @Autowired public MockMvc mockMvc;

  public MvcResult getAnimals() throws Exception {
    return this.mockMvc.perform(get("/animals")).andDo(log()).andReturn();
  }

  public MvcResult getAnimal(final String id) throws Exception {
    return this.mockMvc.perform(get("/animals/" + id)).andDo(log()).andReturn();
  }

  public MvcResult postAnimals(final String content) throws Exception {
    return this.mockMvc
        .perform(post("/animals").contentType(MediaType.APPLICATION_JSON).content(content))
        .andDo(log())
        .andReturn();
  }

  public MvcResult postAnimal(final String id, final String content) throws Exception {
    return this.mockMvc
        .perform(post("/animals/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
        .andDo(log())
        .andReturn();
  }
}
