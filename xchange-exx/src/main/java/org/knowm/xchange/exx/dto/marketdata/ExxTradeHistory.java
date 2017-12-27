package org.knowm.xchange.exx.dto.marketdata;

import java.math.BigDecimal;
import java.util.List;

import org.knowm.xchange.exx.dto.ExxBaseResponse;
import org.knowm.xchange.exx.dto.ExxOrderType;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExxTradeHistory extends ExxBaseResponse {

  private final List<ExxPublicTrade> trades;
  private final String elapsed;

  private ExxTradeHistory(@JsonProperty("data") List<ExxPublicTrade> trades, @JsonProperty("result") boolean result,
      @JsonProperty("elapsed") String elapsed) {

    super(result, null);
    this.trades = trades;
    this.elapsed = elapsed;
  }

  public List<ExxPublicTrade> getTrades() {

    return trades;
  }

  public String getElapsed() {

    return elapsed;
  }

  @Override
  public String toString() {

    return "BTERPublicTrades [trades=" + trades + ", elapsed=" + elapsed + "]";
  }

  public static class ExxPublicTrade {

    private final long date;
    private final BigDecimal price;
    private final BigDecimal amount;
    private final String tradeId;
    private final ExxOrderType type;

    private ExxPublicTrade(@JsonProperty("date") long date, @JsonProperty("price") BigDecimal price, @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("tid") String tradeId, @JsonProperty("type") ExxOrderType type) {

      this.date = date;
      this.price = price;
      this.amount = amount;
      this.tradeId = tradeId;
      this.type = type;
    }

    public long getDate() {

      return date;
    }

    public BigDecimal getPrice() {

      return price;
    }

    public BigDecimal getAmount() {

      return amount;
    }

    public String getTradeId() {

      return tradeId;
    }

    public ExxOrderType getType() {

      return type;
    }

    @Override
    public String toString() {

      return "BTERPublicTrade [date=" + date + ", price=" + price + ", amount=" + amount + ", tradeId=" + tradeId + ", type=" + type + "]";
    }
  }
}
