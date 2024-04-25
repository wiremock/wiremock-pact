package se.bjurr.wiremockpact.wiremockpactspringbootapp.integration.clientfactory;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ResponseLoggingFilter implements ClientResponseFilter {

  private static final Logger LOGGER =
      Logger.getLogger(ResponseLoggingFilter.class.getSimpleName());

  @Override
  public void filter(
      final ClientRequestContext requestContext, final ClientResponseContext responseContext)
      throws IOException {
    final int status = responseContext.getStatus();
    final String statusMsg = responseContext.getStatusInfo().getReasonPhrase();
    final String msg = status + " " + statusMsg;
    if (status >= 200 && status <= 299) {
      LOGGER.log(Level.FINE, "\n<< " + msg + "\n\n");
    } else {
      try (InputStream entityStream = responseContext.getEntityStream()) {
        try (final InputStreamReader in =
            new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
          try (BufferedReader bufferedReader = new BufferedReader(in)) {
            final String entityString = bufferedReader.lines().collect(Collectors.joining("\n"));
            LOGGER.log(Level.SEVERE, "\n<< " + msg + " " + entityString + "\n\n");
          }
        }
      }
    }
  }
}
