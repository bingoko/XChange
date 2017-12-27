package org.knowm.xchange.exx.dto.trade;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exx.dto.ExxOrderType;
import org.knowm.xchange.exx.dto.trade.ExxOpenOrder;
import org.knowm.xchange.exx.dto.trade.ExxOpenOrders;
import org.knowm.xchange.exx.dto.trade.ExxOrderStatus;
import org.knowm.xchange.exx.dto.trade.ExxPlaceOrderReturn;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExxTradeJsonTest {

  @Test
  public void testDeserializeOrderList() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxTradeJsonTest.class.getResourceAsStream("/trade/example-order-list-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxOpenOrders openOrders = mapper.readValue(is, ExxOpenOrders.class);

    assertThat(openOrders.isResult()).isTrue();
    assertThat(openOrders.getMessage()).isEqualTo("Success");

    List<ExxOpenOrder> openOrderList = openOrders.getOrders();
    Assertions.assertThat(openOrderList).hasSize(2);

    ExxOpenOrder openOrder = openOrderList.get(0);
    assertThat(openOrder.getId()).isEqualTo("3");
    assertThat(openOrder.getCurrencyPair().split("_")[0]).isEqualTo("eth");
    assertThat(openOrder.getCurrencyPair().split("_")[1]).isEqualTo("btc");
    assertThat(openOrder.getAmount()).isEqualTo(new BigDecimal("100000"));
  }

  @Test
  public void testDeserializeOrderResult() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxTradeJsonTest.class.getResourceAsStream("/trade/example-order-result-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxPlaceOrderReturn orderReturn = mapper.readValue(is, ExxPlaceOrderReturn.class);

    assertThat(orderReturn.isResult()).isTrue();
    assertThat(orderReturn.getMessage()).isEqualTo("Success");
    assertThat(orderReturn.getOrderId()).isEqualTo("123456");
  }

  @Test
  public void testDeserializeOrderStatus() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = ExxTradeJsonTest.class.getResourceAsStream("/trade/example-order-status-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    ExxOrderStatus orderStatus = mapper.readValue(is, ExxOrderStatus.class);

    assertThat(orderStatus.isResult()).isTrue();
    assertThat(orderStatus.getMessage()).isEqualTo("Success");
    assertThat(orderStatus.getId()).isEqualTo("12942570");
    assertThat(orderStatus.getStatus()).isEqualTo("open");
    assertThat(orderStatus.getCurrencyPair()).isEqualTo(CurrencyPair.LTC_BTC);
    assertThat(orderStatus.getType()).isEqualTo(ExxOrderType.SELL);
    assertThat(orderStatus.getRate()).isEqualTo("0.0265");
    assertThat(orderStatus.getAmount()).isEqualTo("0.384");
    assertThat(orderStatus.getInitialRate()).isEqualTo("0.0265");
    assertThat(orderStatus.getInitialAmount()).isEqualTo("0.384");
  }
}
