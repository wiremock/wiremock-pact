package se.bjurr.wiremockpact.wiremockpactlib.api.pactmodel;

public enum PactInteractionType {
  SYNC_HTTP("Synchronous/HTTP"),
  ASYNC_MESSAGES("Asynchronous/Messages");

  private String typeString;

  PactInteractionType(final String string) {
    this.typeString = string;
  }

  public String getTypeString() {
    return this.typeString;
  }
}
