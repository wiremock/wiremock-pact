package se.bjurr.wiremockpact.wiremockpactspringbootapp.testcases;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils.TestBase;

public class IntegrationTest extends TestBase {

  @Test
  public void testGetAnimals() throws Exception {
    final MvcResult actual = this.getAnimals();

    assertThat(actual.getResponse().getStatus()).isEqualTo(200);

    assertThat(actual.getResponse().getContentAsString())
        .isEqualToIgnoringNewLines("""
	{
	  "animals" : [ {
	    "name" : "Zack"
	  } ]
	}
	""");
  }

  @Test
  public void testGetAnimal() throws Exception {
    final MvcResult actual = this.getAnimal("1");

    assertThat(actual.getResponse().getStatus()).isEqualTo(200);

    assertThat(actual.getResponse().getContentAsString())
        .isEqualToIgnoringNewLines("""
{
  "name" : "Zack"
}
	""");
  }
}
