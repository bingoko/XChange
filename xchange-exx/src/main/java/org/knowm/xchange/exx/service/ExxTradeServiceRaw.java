package org.knowm.xchange.exx.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exx.ExxUtils;
import org.knowm.xchange.exx.dto.ExxBaseResponse;
import org.knowm.xchange.exx.dto.ExxOrderType;
import org.knowm.xchange.exx.dto.trade.ExxOpenOrders;
import org.knowm.xchange.exx.dto.trade.ExxOrderStatus;
import org.knowm.xchange.exx.dto.trade.ExxPlaceOrderReturn;
import org.knowm.xchange.exx.dto.trade.ExxTradeHistoryReturn;

public class ExxTradeServiceRaw extends ExxBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public ExxTradeServiceRaw(Exchange exchange) {

    super(exchange);
  }

  /**
   * Submits a Limit Order to be executed on the Exx Exchange for the desired market defined by {@code CurrencyPair}. WARNING - Exx will return
   * true regardless of whether or not an order actually gets created. The reason for this is that orders are simply submitted to a queue in their
   * back-end. One example for why an order might not get created is because there are insufficient funds. The best attempt you can make to confirm
   * that the order was created is to poll {@link #getExxOpenOrders}. However if the order is created and executed before it is caught in its open
   * state from calling {@link #getExxOpenOrders} then the only way to confirm would be confirm the expected difference in funds available for your
   * account.
   *
   * @param limitOrder
   * @return String order id of submitted request.
   * @throws IOException
   */
  public String placeExxLimitOrder(LimitOrder limitOrder) throws IOException {

    ExxOrderType type = (limitOrder.getType() == Order.OrderType.BID) ? ExxOrderType.BUY : ExxOrderType.SELL;

    return placeExxLimitOrder(limitOrder.getCurrencyPair(), type, limitOrder.getLimitPrice(), limitOrder.getOriginalAmount());
  }

  /**
   * Submits a Limit Order to be executed on the Gateio Exchange for the desired market defined by {@code currencyPair}. WARNING - Gateio will return
   * true regardless of whether or not an order actually gets created. The reason for this is that orders are simply submitted to a queue in their
   * back-end. One example for why an order might not get created is because there are insufficient funds. The best attempt you can make to confirm
   * that the order was created is to poll {@link #getGateioOpenOrders}. However if the order is created and executed before it is caught in its open
   * state from calling {@link #getGateioOpenOrders} then the only way to confirm would be confirm the expected difference in funds available for your
   * account.
   *
   * @param currencyPair
   * @param orderType
   * @param rate
   * @param amount
   * @return String order id of submitted request.
   * @throws IOException
   */
  public String placeExxLimitOrder(CurrencyPair currencyPair, ExxOrderType orderType, BigDecimal rate, BigDecimal amount) throws IOException {

    String pair = formatCurrencyPair(currencyPair);

    ExxPlaceOrderReturn orderId;
    if (orderType.equals(ExxOrderType.BUY)) {
      orderId = bter.buy(pair, rate, amount, apiKey, signatureCreator, exchange.getNonceFactory());
    } else {
      orderId = bter.sell(pair, rate, amount, apiKey, signatureCreator, exchange.getNonceFactory());
    }

    return handleResponse(orderId).getOrderId();
  }

  public boolean cancelOrder(String orderId) throws IOException {

    ExxBaseResponse cancelOrderResult = bter.cancelOrder(orderId, apiKey, signatureCreator, exchange.getNonceFactory());

    return handleResponse(cancelOrderResult).isResult();
  }

  /**
   * Cancels all orders.
   * See https://www.exx.com/help/restApi
   * @param type order type(0:sell,1:buy,-1:all)
   * @param currencyPair currency pair
   * @return
   * @throws IOException
   */
  public boolean cancelAllOrders(String type, CurrencyPair currencyPair) throws IOException {

    ExxBaseResponse cancelAllOrdersResult = bter.cancelAllOrders(type, formatCurrencyPair(currencyPair), apiKey, signatureCreator, exchange.getNonceFactory());

    return handleResponse(cancelAllOrdersResult).isResult();
  }

  public ExxOpenOrders getExxOpenOrders() throws IOException {

    ExxOpenOrders exxOpenOrdersReturn = bter.getOpenOrders(apiKey, signatureCreator, exchange.getNonceFactory());

    return handleResponse(exxOpenOrdersReturn);
  }

  public ExxOrderStatus getExxOrderStatus(String orderId) throws IOException {

    ExxOrderStatus orderStatus = bter.getOrderStatus(orderId, apiKey, signatureCreator, exchange.getNonceFactory());

    return handleResponse(orderStatus);
  }

  public ExxTradeHistoryReturn getExxTradeHistory(CurrencyPair currencyPair) throws IOException {

    ExxTradeHistoryReturn exxTradeHistoryReturn = bter.getUserTradeHistory(apiKey, signatureCreator, exchange.getNonceFactory(),
        ExxUtils.toPairString(currencyPair));

    return handleResponse(exxTradeHistoryReturn);
  }

  private String formatCurrencyPair(CurrencyPair currencyPair) {
    return String.format("%s_%s", currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode()).toLowerCase();
  }

}
