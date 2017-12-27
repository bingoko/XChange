package org.knowm.xchange.exx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExxBaseResponse {

  private final boolean result;
  private final String message;

  protected ExxBaseResponse(@JsonProperty("result") final boolean result, @JsonProperty("msg") final String message) {

    this.result = result;
    this.message = message;
  }

  public boolean isResult() {

    return result;
  }

  public String getMessage() {

    return message;
  }

  @Override
  public String toString() {

    return "GateioBaseResponse [result=" + result + ", message=" + message + "]";
  }
}
