package se.bjurr.wiremockpact.wiremockpactlib.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.Pact;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactBody;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactInteraction;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactRequest;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactResponse;

// Inspired by:
// https://bitbucket.org/atlassian/wiremock-pact-generator/src/master/src/main/java/com/atlassian/ta/wiremockpactgenerator/pactgenerator/json/
public class JsonHelper {
  public static class PactSerializer implements JsonSerializer<Pact> {
    @Override
    public JsonElement serialize(
        final Pact pact, final Type type, final JsonSerializationContext context) {
      final JsonObject object = new JsonObject();
      object.add("consumer", context.serialize(pact.getConsumer()));
      object.add("interactions", context.serialize(pact.getInteractions()));
      object.add("metadata", context.serialize(pact.getMetadata()));
      object.add("provider", context.serialize(pact.getProvider()));
      return object;
    }
  }

  public static class PactInteractionSerializer implements JsonSerializer<PactInteraction> {
    @Override
    public JsonElement serialize(
        final PactInteraction pact, final Type type, final JsonSerializationContext context) {
      final JsonObject object = new JsonObject();
      object.add("description", context.serialize(pact.getDescription()));
      object.add("key", context.serialize(pact.getKey()));
      object.add("pending", context.serialize(pact.isPending()));
      if (!pact.getProviderStates().isEmpty()) {
        object.add("providerStates", context.serialize(pact.getProviderStates()));
      }
      object.add("request", context.serialize(pact.getRequest()));
      object.add("response", context.serialize(pact.getResponse()));
      object.add("type", context.serialize(pact.getType()));
      return object;
    }
  }

  public abstract static class PactHttpBodySerializer implements JsonSerializer<PactBody> {
    @Override
    public JsonElement serialize(
        final PactBody body,
        final Type type,
        final JsonSerializationContext jsonSerializationContext) {

      final String bodyValue = body.getContent();
      JsonElement content = null;
      if (bodyValue == null || bodyValue.isEmpty()) {
        content = JsonParser.parseString("\"\"");
      } else if (this.shouldSerializeAsJson(body.getContent())) {
        content = JsonParser.parseString(bodyValue);
      } else {
        content = new JsonPrimitive(body.getContent());
      }

      final JsonObject jsonRequest = new JsonObject();
      jsonRequest.add("content", content);
      if (body.getContentType() != null) {
        jsonRequest.addProperty("contentType", body.getContentType());
      }
      if (body.getEncoded() != null) {
        jsonRequest.addProperty("encoded", body.getEncoded());
      }
      return jsonRequest;
    }

    protected abstract boolean shouldSerializeElement(final JsonElement element);

    private boolean shouldSerializeAsJson(final String s) {
      try {
        final JsonElement element = JsonParser.parseString(s);
        return this.shouldSerializeElement(element);
      } catch (final JsonSyntaxException ex) {
        return false;
      }
    }
  }

  public static class PactRequestSerializer implements JsonSerializer<PactRequest> {

    @Override
    public JsonElement serialize(
        final PactRequest request, final Type type, final JsonSerializationContext context) {
      final JsonObject jsonRequest = new JsonObject();
      jsonRequest.add("body", context.serialize(request.getBody()));
      if (request.getHeaders() != null) {
        jsonRequest.add("headers", context.serialize(request.getHeaders()));
      }
      jsonRequest.addProperty("method", request.getMethod());
      jsonRequest.addProperty("path", request.getPath());
      if (request.getQuery() != null) {
        jsonRequest.add("query", context.serialize(request.getQuery()));
      }
      return jsonRequest;
    }
  }

  public static class PactResponseSerializer implements JsonSerializer<PactResponse> {

    @Override
    public JsonElement serialize(
        final PactResponse response, final Type type, final JsonSerializationContext context) {
      final JsonObject jsonResponse = new JsonObject();
      jsonResponse.add("body", context.serialize(response.getBody()));
      if (response.getHeaders() != null && !response.getHeaders().isEmpty()) {
        jsonResponse.add("headers", context.serialize(response.getHeaders()));
      }
      jsonResponse.addProperty("status", response.getStatus());
      return jsonResponse;
    }
  }

  public static class PactBodySerializer extends PactHttpBodySerializer {
    @Override
    protected boolean shouldSerializeElement(final JsonElement element) {
      return true;
    }
  }

  public static Gson GSON =
      new GsonBuilder()
          .registerTypeAdapter(Pact.class, new PactSerializer())
          .registerTypeAdapter(PactInteraction.class, new PactInteractionSerializer())
          .registerTypeAdapter(PactRequest.class, new PactRequestSerializer())
          .registerTypeAdapter(PactResponse.class, new PactResponseSerializer())
          .registerTypeAdapter(PactBody.class, new PactBodySerializer())
          .disableHtmlEscaping()
          .serializeNulls()
          .setPrettyPrinting()
          .create();

  public static String toJson(final Object obj) {
    if (obj == null) {
      return "{}";
    }
    return GSON.toJson(obj);
  }

  public static <T> T fromJson(final Class<T> clazz, final String json) {
    return GSON.fromJson(json, clazz);
  }
}
