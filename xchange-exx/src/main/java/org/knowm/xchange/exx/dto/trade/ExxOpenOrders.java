package org.knowm.xchange.exx.dto.trade;

import java.util.List;

import org.knowm.xchange.exx.dto.ExxBaseResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by David Henry on 2/19/14.
 */
public class ExxOpenOrders extends ExxBaseResponse {

  private final List<ExxOpenOrder> orders;

  /**
   * Constructor
   *
   * @param result
   * @param orders
   * @param msg
   */
  public ExxOpenOrders(@JsonProperty("result") Boolean result, @JsonProperty("orders") List<ExxOpenOrder> orders, @JsonProperty("msg") String msg) {

    super(result, msg);
    this.orders = orders;
  }

  public List<ExxOpenOrder> getOrders() {

    return orders;
  }

  @Override
  public String toString() {

    return "BTEROpenOrdersReturn [orders=" + orders + "]";
  }

}
