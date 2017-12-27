package org.knowm.xchange.exx.dto.marketdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exx.dto.ExxOrderType;
import org.knowm.xchange.exx.dto.marketdata.ExxCurrencyPairs;
import org.knowm.xchange.exx.dto.marketdata.ExxDepth;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper;
import org.knowm.xchange.exx.dto.marketdata.ExxPublicOrder;
import org.knowm.xchange.exx.dto.marketdata.ExxTradeHistory;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper.exxMarketInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExxMarketDataJsonTest {

  @Test
  public void testDeserializeMarketInfo() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxMarketDataJsonTest.class.getResourceAsStream("/marketdata/example-market-info-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxMarketInfoWrapper marketInfoWrapper = mapper.readValue(is, ExxMarketInfoWrapper.class);

    Map<CurrencyPair, exxMarketInfo> marketInfoMap = marketInfoWrapper.getMarketInfoMap();
    assertThat(marketInfoMap).hasSize(2);

    CurrencyPair pair = new CurrencyPair("LTC", "CNY");
    exxMarketInfo marketInfo = marketInfoMap.get(pair);
    assertThat(marketInfo.getCurrencyPair()).isEqualTo(pair);
    assertThat(marketInfo.getDecimalPlaces()).isEqualTo(2);
    assertThat(marketInfo.getMinAmount()).isEqualTo(".5");
    assertThat(marketInfo.getFee()).isEqualTo("0");
  }

  @Test
  public void testDeserializeCurrencyPairs() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxMarketDataJsonTest.class.getResourceAsStream("/marketdata/example-pairs-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxCurrencyPairs currencyPairs = mapper.readValue(is, ExxCurrencyPairs.class);

    Collection<CurrencyPair> pairs = currencyPairs.getPairs();
    assertThat(pairs).hasSize(83);

    assertThat(pairs.contains(new CurrencyPair("TIPS", "CNY"))).isTrue();
  }

  @Test
  public void testDeserializeDepth() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxMarketDataJsonTest.class.getResourceAsStream("/marketdata/example-depth-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxDepth depth = mapper.readValue(is, ExxDepth.class);

    assertThat(depth.isResult()).isTrue();

    List<ExxPublicOrder> asks = depth.getAsks();
    assertThat(asks).hasSize(3);

    ExxPublicOrder ask = asks.get(0);
    assertThat(ask.getPrice()).isEqualTo("0.17936");
    assertThat(ask.getAmount()).isEqualTo("687");
  }

  @Test
  public void testDeserializeTrades() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxMarketDataJsonTest.class.getResourceAsStream("/marketdata/example-trades-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxTradeHistory tradeHistory = mapper.readValue(is, ExxTradeHistory.class);

    assertThat(tradeHistory.isResult()).isTrue();
    assertThat(tradeHistory.getElapsed()).isEqualTo("0.634ms");

    List<ExxTradeHistory.ExxPublicTrade> trades = tradeHistory.getTrades();
    assertThat(trades).hasSize(2);

    ExxTradeHistory.ExxPublicTrade trade = trades.get(0);
    assertThat(trade.getDate()).isEqualTo(1393908191);
    assertThat(trade.getPrice()).isEqualTo(new BigDecimal("3942"));
    assertThat(trade.getAmount()).isEqualTo(new BigDecimal("0.0129"));
    assertThat(trade.getTradeId()).isEqualTo("5600118");
    Assertions.assertThat(trade.getType()).isEqualTo(ExxOrderType.SELL);
  }
}
