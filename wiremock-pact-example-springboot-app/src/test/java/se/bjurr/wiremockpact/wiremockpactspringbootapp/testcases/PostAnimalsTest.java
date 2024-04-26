package se.bjurr.wiremockpact.wiremockpactspringbootapp.testcases;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils.TestBase;

public class PostAnimalsTest extends TestBase {

  @Test
  public void testGetAnimals() throws Exception {
    final MvcResult actual = this.postAnimals("""
    		{
    		  "animals" : [ {
    		    "name" : "Zack"
    		  } ]
    		}
    		""");

    assertThat(actual.getResponse().getStatus()).isEqualTo(200);
  }

@Test
  public void testGetAnimal() throws Exception {
    final MvcResult actual = this.postAnimal("1","""
    		{
    		  "name" : "Zack"
    		}
    			""");

    assertThat(actual.getResponse().getStatus()).isEqualTo(200);
  }
}
