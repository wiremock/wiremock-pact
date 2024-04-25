package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.clientfactory;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import java.net.URI;
import java.net.URISyntaxException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.jboss.resteasy.plugins.providers.StringTextStar;

public class RestEasyClientFactory {
  public static <T> T create(final Class<T> clazz, final String basePath) {

    final ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setSerializationInclusion(Include.NON_DEFAULT);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    final JacksonJsonProvider jaxbJsonProvider =
        new JacksonJsonProvider(mapper, JacksonJsonProvider.BASIC_ANNOTATIONS);
    jaxbJsonProvider.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    jaxbJsonProvider.enable(SerializationFeature.INDENT_OUTPUT);

    URI targetUri;
    try {
      targetUri = new URI(basePath);
    } catch (final URISyntaxException e) {
      throw new RuntimeException(basePath, e);
    }
    final ResteasyClient client = // NOPMD
        new ResteasyClientBuilderImpl() //
            .connectTimeout(10, SECONDS) //
            .readTimeout(10, SECONDS) //
            .connectionCheckoutTimeout(10, SECONDS) //
            .register(new RequestLoggingFilter()) //
            .register(new ResponseLoggingFilter()) //
            .register(jaxbJsonProvider) //
            .register(new StringTextStar()) //
            .build();

    final ResteasyWebTarget target = client.target(targetUri);
    return target.proxy(clazz);
  }
}
