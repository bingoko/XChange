package org.knowm.xchange.exx.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.exx.ExxAdapters;
import org.knowm.xchange.exx.dto.marketdata.ExxDepth;
import org.knowm.xchange.exx.dto.marketdata.ExxTicker;
import org.knowm.xchange.exx.dto.marketdata.ExxTradeHistory;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class ExxMarketDataService extends ExxMarketDataServiceRaw implements MarketDataService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public ExxMarketDataService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {

    ExxTicker ticker = super.getBTERTicker(currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode());

    return ExxAdapters.adaptTicker(currencyPair, ticker);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {

    ExxDepth exxDepth = super.getBTEROrderBook(currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode());

    return ExxAdapters.adaptOrderBook(exxDepth, currencyPair);
  }

  public Map<CurrencyPair, OrderBook> getOrderBooks() throws IOException {

    Map<CurrencyPair, ExxDepth> exxDepths = super.getExxDepths();
    Map<CurrencyPair, OrderBook> orderBooks = new HashMap<>(exxDepths.size());

    exxDepths.forEach((currencyPair, exxDepth) -> {
      OrderBook orderBook = ExxAdapters.adaptOrderBook(exxDepth, currencyPair);
      orderBooks.put(currencyPair, orderBook);
    });

    return orderBooks;
  }

  @Override
  public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {

    ExxTradeHistory tradeHistory = (args != null && args.length > 0 && args[0] != null && args[0] instanceof String)
        ? super.getBTERTradeHistorySince(currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode(), (String) args[0])
        : super.getBTERTradeHistory(currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode());

    return ExxAdapters.adaptTrades(tradeHistory, currencyPair);
  }

}
