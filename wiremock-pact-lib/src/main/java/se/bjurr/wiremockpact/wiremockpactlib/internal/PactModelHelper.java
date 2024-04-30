package se.bjurr.wiremockpact.wiremockpactlib.internal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.stream.Collectors;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.Pact;
import se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel.PactInteraction;

public class PactModelHelper {

  public static String generateKey(final PactInteraction interaction) {
    final String str =
        interaction.getDescription()
            + interaction.getProviderStates().stream()
                .sorted()
                .map(it -> it.getName())
                .collect(Collectors.joining());
    return sha256(str);
  }

  private static String sha256(final String base) {
    // https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha-256-in-java
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
      final StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
        final String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void writeToFile(final Pact pact, final String absoluteJsonFolder) {
    final String pactJson = JsonHelper.toJson(pact);
    final Path targetFolder = Paths.get(absoluteJsonFolder);
    targetFolder.toFile().mkdirs();
    final String fileName =
        pact.getConsumer().getName() + "-" + pact.getProvider().getName() + ".json";
    for (final PactInteraction interaction : pact.getInteractions()) {
      final Path targetFile = targetFolder.resolve(fileName);
      try {
        Files.write(targetFile, pactJson.getBytes(StandardCharsets.UTF_8));
      } catch (final IOException e) {
        throw new RuntimeException(targetFile.toString(), e);
      }
    }
  }
}
