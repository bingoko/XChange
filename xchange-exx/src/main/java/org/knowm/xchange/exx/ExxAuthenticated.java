package org.knowm.xchange.exx;

import java.io.IOException;
import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.exx.dto.ExxBaseResponse;
import org.knowm.xchange.exx.dto.account.ExxFunds;
import org.knowm.xchange.exx.dto.trade.ExxOpenOrders;
import org.knowm.xchange.exx.dto.trade.ExxOrderStatus;
import org.knowm.xchange.exx.dto.trade.ExxPlaceOrderReturn;
import org.knowm.xchange.exx.dto.trade.ExxTradeHistoryReturn;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("api2/1")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public interface ExxAuthenticated extends Exx {

  @POST
  @Path("private/balances")
  ExxFunds getFunds(@HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  @POST
  @Path("private/cancelorder")
  ExxBaseResponse cancelOrder(@FormParam("orderNumber") String orderNumber, @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  @POST
  @Path("private/cancelAllOrders")
  ExxBaseResponse cancelAllOrders(@FormParam("type") String type, @FormParam("currencyPair") String currencyPair, @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  @POST
  @Path("private/buy")
  ExxPlaceOrderReturn buy(@FormParam("currencyPair") String currencyPair, @FormParam("rate") BigDecimal rate,
      @FormParam("amount") BigDecimal amount, @HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  @POST
  @Path("private/sell")
  ExxPlaceOrderReturn sell(@FormParam("currencyPair") String currencyPair, @FormParam("rate") BigDecimal rate,
      @FormParam("amount") BigDecimal amount, @HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  @POST
  @Path("private/openOrders")
  ExxOpenOrders getOpenOrders(@HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  @POST
  @Path("private/tradeHistory")
  ExxTradeHistoryReturn getUserTradeHistory(@HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("pair") String pair) throws IOException;

  @POST
  @Path("private/getorder")
  ExxOrderStatus getOrderStatus(@FormParam("order_id") String orderId, @HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;
}
