package org.knowm.xchange.exx.dto.trade;

import org.knowm.xchange.exx.dto.ExxBaseResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExxPlaceOrderReturn extends ExxBaseResponse {

  private final String orderNumber;

  /**
   * Constructor
   */
  private ExxPlaceOrderReturn(@JsonProperty("result") boolean result, @JsonProperty("orderNumber") String orderNumber,
      @JsonProperty("msg") String message) {

    super(result, message);
    this.orderNumber = orderNumber;
  }

  public String getOrderId() {

    return orderNumber;
  }

  @Override
  public String toString() {

    return "GateioPlaceOrderReturn [orderNumber=" + orderNumber + "]";
  }

}
