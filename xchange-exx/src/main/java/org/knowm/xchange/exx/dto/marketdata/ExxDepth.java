package org.knowm.xchange.exx.dto.marketdata;

import java.util.List;

import org.knowm.xchange.exx.dto.ExxBaseResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data object representing depth from Bter
 */
public class ExxDepth extends ExxBaseResponse {

  private final List<ExxPublicOrder> asks;
  private final List<ExxPublicOrder> bids;

  /**
   * Constructor
   *
   * @param asks
   * @param bids
   */
  private ExxDepth(@JsonProperty("asks") List<ExxPublicOrder> asks, @JsonProperty("bids") List<ExxPublicOrder> bids,
      @JsonProperty("result") boolean result) {

    super(result, null);
    this.asks = asks;
    this.bids = bids;
  }

  public List<ExxPublicOrder> getAsks() {

    return asks;
  }

  public List<ExxPublicOrder> getBids() {

    return bids;
  }

  @Override
  public String toString() {

    return "GateioDepth [asks=" + asks.toString() + ", bids=" + bids.toString() + "]";
  }

}
