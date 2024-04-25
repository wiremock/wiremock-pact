package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestLoggingFilter implements ClientRequestFilter {

  private static final Logger LOGGER =
      Logger.getLogger(ResponseLoggingFilter.class.getSimpleName());

  @Override
  public void filter(final ClientRequestContext requestContext) throws IOException {
    final String path = requestContext.getUri().getPath();
    final String method = requestContext.getMethod();
    final String msg =
        "\n>> "
            + method
            + " "
            + path
            + " "
            + (requestContext.getEntity() == null ? "" : "\n" + requestContext.getEntity())
            + "\n\n";
    LOGGER.log(Level.FINE, msg);
  }
}
