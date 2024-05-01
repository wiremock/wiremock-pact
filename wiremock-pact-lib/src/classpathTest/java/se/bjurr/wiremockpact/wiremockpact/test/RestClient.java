package se.bjurr.wiremockpact.wiremockpact.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestClient {

  public static String doRestCallGet(final String url, final String accept) throws Exception {
    final URL serverAddress = new URL(url);
    HttpURLConnection connection = null;
    connection = (HttpURLConnection) serverAddress.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Accept", accept);
    connection.setDoOutput(true);
    connection.setReadTimeout(10000);
    connection.connect();
    final BufferedReader rd =
        new BufferedReader(new InputStreamReader(connection.getInputStream()));
    final StringBuilder sb = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      sb.append(line + '\n');
    }
    return sb.toString();
  }

  public static String doRestCallPost(final String url, final String contentType, final String body)
      throws Exception {
    final URL serverAddress = new URL(url);
    final HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", contentType);
    connection.setRequestProperty("Content-Length", "" + body.getBytes().length);
    connection.setRequestProperty("Accept", contentType);
    connection.setDoOutput(true);
    connection.setReadTimeout(10000);
    connection.connect();

    final OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
    wr.write(body);
    wr.flush();

    final BufferedReader rd =
        new BufferedReader(new InputStreamReader(connection.getInputStream()));
    final StringBuilder sb = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      sb.append(line + '\n');
    }
    return sb.toString();
  }
}
