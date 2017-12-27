package org.knowm.xchange.exx.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.exx.ExxAdapters;
import org.knowm.xchange.exx.dto.marketdata.ExxDepth;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper;
import org.knowm.xchange.exx.dto.marketdata.ExxTicker;
import org.knowm.xchange.exx.dto.marketdata.ExxTradeHistory;

public class ExxMarketDataServiceRaw extends ExxBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public ExxMarketDataServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public Map<CurrencyPair, ExxMarketInfoWrapper.exxMarketInfo> getBTERMarketInfo() throws IOException {

    ExxMarketInfoWrapper bterMarketInfo = bter.getMarketInfo();

    return bterMarketInfo.getMarketInfoMap();
  }

  public Map<CurrencyPair, Ticker> getBTERTickers() throws IOException {

    Map<String, ExxTicker> exxTickers = bter.getTickers();
    Map<CurrencyPair, Ticker> adaptedTickers = new HashMap<>(exxTickers.size());
    exxTickers.forEach((currencyPairString, exxTicker) -> {
      String[] currencyPairStringSplit = currencyPairString.split("_");
      CurrencyPair currencyPair = new CurrencyPair(new Currency(currencyPairStringSplit[0].toUpperCase()), new Currency(currencyPairStringSplit[1].toUpperCase()));
      adaptedTickers.put(currencyPair, ExxAdapters.adaptTicker(currencyPair, exxTicker));
    });

    return adaptedTickers;
  }

  public Map<CurrencyPair, ExxDepth> getExxDepths() throws IOException {
    Map<String, ExxDepth> depths = bter.getDepths();
    Map<CurrencyPair, ExxDepth> adaptedDepths = new HashMap<>(depths.size());
    depths.forEach((currencyPairString, exxDepth) -> {
      String[] currencyPairStringSplit = currencyPairString.split("_");
      CurrencyPair currencyPair = new CurrencyPair(new Currency(currencyPairStringSplit[0].toUpperCase()), new Currency(currencyPairStringSplit[1].toUpperCase()));
      adaptedDepths.put(currencyPair, exxDepth);
    });
    return adaptedDepths;
  }

  public ExxTicker getBTERTicker(String tradableIdentifier, String currency) throws IOException {

    ExxTicker exxTicker = bter.getTicker(tradableIdentifier.toLowerCase(), currency.toLowerCase());

    return handleResponse(exxTicker);
  }

  public ExxDepth getBTEROrderBook(String tradeableIdentifier, String currency) throws IOException {

    ExxDepth exxDepth = bter.getFullDepth(tradeableIdentifier.toLowerCase(), currency.toLowerCase());

    return handleResponse(exxDepth);
  }

  public ExxTradeHistory getBTERTradeHistory(String tradeableIdentifier, String currency) throws IOException {

    ExxTradeHistory tradeHistory = bter.getTradeHistory(tradeableIdentifier, currency);

    return handleResponse(tradeHistory);
  }

  public ExxTradeHistory getBTERTradeHistorySince(String tradeableIdentifier, String currency, String tradeId) throws IOException {

    ExxTradeHistory tradeHistory = bter.getTradeHistorySince(tradeableIdentifier, currency, tradeId);

    return handleResponse(tradeHistory);
  }

  public List<CurrencyPair> getExchangeSymbols() throws IOException {

    List<CurrencyPair> currencyPairs = new ArrayList<>(bter.getPairs().getPairs());
    return currencyPairs;
  }
}
