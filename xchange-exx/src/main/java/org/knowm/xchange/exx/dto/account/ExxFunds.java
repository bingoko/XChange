package org.knowm.xchange.exx.dto.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.knowm.xchange.exx.dto.ExxBaseResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExxFunds extends ExxBaseResponse {

  private final Map<String, BigDecimal> available;
  private final Map<String, BigDecimal> locked;

  /**
   * Constructor
   *
   * @param theAvailable
   * @param theLocked
   */
  public ExxFunds(@JsonProperty("available") Map<String, BigDecimal> theAvailable,
      @JsonProperty("locked") Map<String, BigDecimal> theLocked, @JsonProperty("result") boolean result,
      @JsonProperty("message") final String message) {

    super(result, message);

    available = theAvailable == null ? new HashMap<String, BigDecimal>() : theAvailable;
    locked = theLocked == null ? new HashMap<String, BigDecimal>() : theLocked;

  }

  public Map<String, BigDecimal> getAvailableFunds() {

    return available;
  }

  public Map<String, BigDecimal> getLockedFunds() {

    return locked;
  }

  @Override
  public String toString() {

    return "BTERAccountInfoReturn [availableFunds=" + available + ", lockedFunds=" + locked + "]";
  }

}
