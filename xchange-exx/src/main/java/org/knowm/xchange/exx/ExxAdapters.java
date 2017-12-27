package org.knowm.xchange.exx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.marketdata.Trades.TradeSortType;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exx.dto.ExxOrderType;
import org.knowm.xchange.exx.dto.account.ExxFunds;
import org.knowm.xchange.exx.dto.marketdata.ExxDepth;
import org.knowm.xchange.exx.dto.marketdata.ExxPublicOrder;
import org.knowm.xchange.exx.dto.marketdata.ExxTicker;
import org.knowm.xchange.exx.dto.marketdata.ExxTradeHistory;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper.exxMarketInfo;
import org.knowm.xchange.exx.dto.trade.ExxOpenOrder;
import org.knowm.xchange.exx.dto.trade.ExxOpenOrders;
import org.knowm.xchange.exx.dto.trade.ExxTrade;
import org.knowm.xchange.utils.DateUtils;

/**
 * Various adapters for converting from Bter DTOs to XChange DTOs
 */
public final class ExxAdapters {

  /**
   * private Constructor
   */
  private ExxAdapters() {

  }

  public static CurrencyPair adaptCurrencyPair(String pair) {

    final String[] currencies = pair.toUpperCase().split("_");
    return new CurrencyPair(currencies[0], currencies[1]);
  }

  public static Ticker adaptTicker(CurrencyPair currencyPair, ExxTicker exxTicker) {

    BigDecimal ask = exxTicker.getLowestAsk();
    BigDecimal bid = exxTicker.getHighestBid();
    BigDecimal last = exxTicker.getLast();
    BigDecimal low = exxTicker.getLow24hr();
    BigDecimal high = exxTicker.getHigh24hr();
    // Looks like exx.com vocabulary is inverted...
    BigDecimal baseVolume = exxTicker.getQuoteVolume();
    BigDecimal quoteVolume = exxTicker.getBaseVolume();

    return new Ticker.Builder().currencyPair(currencyPair).ask(ask).bid(bid).last(last).low(low).high(high).volume(baseVolume).quoteVolume(quoteVolume).build();
  }

  public static LimitOrder adaptOrder(ExxPublicOrder order, CurrencyPair currencyPair, OrderType orderType) {

    return new LimitOrder(orderType, order.getAmount(), currencyPair, "", null, order.getPrice());
  }

  public static List<LimitOrder> adaptOrders(List<ExxPublicOrder> orders, CurrencyPair currencyPair, OrderType orderType) {

    List<LimitOrder> limitOrders = new ArrayList<>();

    for (ExxPublicOrder bterOrder : orders) {
      limitOrders.add(adaptOrder(bterOrder, currencyPair, orderType));
    }

    return limitOrders;
  }

  public static OrderBook adaptOrderBook(ExxDepth depth, CurrencyPair currencyPair) {

    List<LimitOrder> asks = ExxAdapters.adaptOrders(depth.getAsks(), currencyPair, OrderType.ASK);
    Collections.reverse(asks);
    List<LimitOrder> bids = ExxAdapters.adaptOrders(depth.getBids(), currencyPair, OrderType.BID);

    return new OrderBook(null, asks, bids);
  }

  public static LimitOrder adaptOrder(ExxOpenOrder order, Collection<CurrencyPair> currencyPairs) {

    String[] currencyPairSplit = order.getCurrencyPair().split("_");
    CurrencyPair currencyPair = new CurrencyPair(currencyPairSplit[0], currencyPairSplit[1]);
    return new LimitOrder(order.getType().equals("sell") ? OrderType.ASK : OrderType.BID, order.getAmount(), currencyPair, order.getOrderNumber(),
        null, order.getRate());
  }

  public static OpenOrders adaptOpenOrders(ExxOpenOrders openOrders, Collection<CurrencyPair> currencyPairs) {

    List<LimitOrder> adaptedOrders = new ArrayList<>();
    for (ExxOpenOrder openOrder : openOrders.getOrders()) {
      adaptedOrders.add(adaptOrder(openOrder, currencyPairs));
    }

    return new OpenOrders(adaptedOrders);
  }

  public static OrderType adaptOrderType(ExxOrderType cryptoTradeOrderType) {

    return (cryptoTradeOrderType.equals(ExxOrderType.BUY)) ? OrderType.BID : OrderType.ASK;
  }

  public static Trade adaptTrade(ExxTradeHistory.ExxPublicTrade trade, CurrencyPair currencyPair) {

    OrderType orderType = adaptOrderType(trade.getType());
    Date timestamp = DateUtils.fromMillisUtc(trade.getDate() * 1000);

    return new Trade(orderType, trade.getAmount(), currencyPair, trade.getPrice(), timestamp, trade.getTradeId());
  }

  public static Trades adaptTrades(ExxTradeHistory tradeHistory, CurrencyPair currencyPair) {

    List<Trade> tradeList = new ArrayList<>();
    long lastTradeId = 0;
    for (ExxTradeHistory.ExxPublicTrade trade : tradeHistory.getTrades()) {
      String tradeIdString = trade.getTradeId();
      if (!tradeIdString.isEmpty()) {
        long tradeId = Long.valueOf(tradeIdString);
        if (tradeId > lastTradeId) {
          lastTradeId = tradeId;
        }
      }
      Trade adaptedTrade = adaptTrade(trade, currencyPair);
      tradeList.add(adaptedTrade);
    }

    return new Trades(tradeList, lastTradeId, TradeSortType.SortByTimestamp);
  }

  public static Wallet adaptWallet(ExxFunds bterAccountInfo) {

    List<Balance> balances = new ArrayList<>();
    for (Entry<String, BigDecimal> funds : bterAccountInfo.getAvailableFunds().entrySet()) {
      Currency currency = Currency.getInstance(funds.getKey().toUpperCase());
      BigDecimal amount = funds.getValue();
      BigDecimal locked = bterAccountInfo.getLockedFunds().get(currency.toString());

      balances.add(new Balance(currency, null, amount, locked == null ? BigDecimal.ZERO : locked));
    }
    for (Entry<String, BigDecimal> funds : bterAccountInfo.getLockedFunds().entrySet()) {
      Currency currency = Currency.getInstance(funds.getKey().toUpperCase());
      if (balances.stream().noneMatch(balance -> balance.getCurrency().equals(currency))) {
        BigDecimal amount = funds.getValue();
        balances.add(new Balance(currency, null, BigDecimal.ZERO, amount));
      }
    }

    return new Wallet(balances);
  }

  public static UserTrades adaptUserTrades(List<ExxTrade> userTrades) {

    List<UserTrade> trades = new ArrayList<>();
    for (ExxTrade userTrade : userTrades) {
      trades.add(adaptUserTrade(userTrade));
    }

    return new UserTrades(trades, TradeSortType.SortByTimestamp);
  }

  public static UserTrade adaptUserTrade(ExxTrade exxTrade) {

    OrderType orderType = adaptOrderType(exxTrade.getType());
    Date timestamp = DateUtils.fromMillisUtc(exxTrade.getTimeUnix() * 1000);
    CurrencyPair currencyPair = adaptCurrencyPair(exxTrade.getPair());

    return new UserTrade(orderType, exxTrade.getAmount(), currencyPair, exxTrade.getRate(), timestamp, exxTrade.getId(), null, null,
        (Currency) null);
  }

  public static ExchangeMetaData adaptToExchangeMetaData(Map<CurrencyPair, exxMarketInfo> currencyPair2BTERMarketInfoMap) {

    Map<CurrencyPair, CurrencyPairMetaData> currencyPairs = new HashMap<>();

    for (Entry<CurrencyPair, exxMarketInfo> entry : currencyPair2BTERMarketInfoMap.entrySet()) {

      CurrencyPair currencyPair = entry.getKey();
      exxMarketInfo btermarketInfo = entry.getValue();

      CurrencyPairMetaData currencyPairMetaData = new CurrencyPairMetaData(btermarketInfo.getFee(), btermarketInfo.getMinAmount(), null,
          btermarketInfo.getDecimalPlaces());
      currencyPairs.put(currencyPair, currencyPairMetaData);
    }

    ExchangeMetaData exchangeMetaData = new ExchangeMetaData(currencyPairs, null, null, null, null);

    return exchangeMetaData;
  }

}
