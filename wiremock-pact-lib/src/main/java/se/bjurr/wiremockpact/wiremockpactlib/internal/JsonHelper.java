package se.bjurr.wiremockpact.wiremockpactlib.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
  public static String toJson(final Object obj) {
    if (obj == null) {
      return "{}";
    }
    try {
      return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }

  public static <T> T fromJson(final Class<T> clazz, final String json) {
    try {
      return new ObjectMapper().readValue(json, clazz);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(json, e);
    }
  }
}
