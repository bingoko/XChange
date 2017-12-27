package org.knowm.xchange.exx;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.exx.dto.marketdata.ExxCurrencyPairs;
import org.knowm.xchange.exx.dto.marketdata.ExxDepth;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper;
import org.knowm.xchange.exx.dto.marketdata.ExxTicker;
import org.knowm.xchange.exx.dto.marketdata.ExxTradeHistory;

@Path("api2/1")
@Produces(MediaType.APPLICATION_JSON)
public interface Exx {

  @GET
  @Path("marketinfo")
  ExxMarketInfoWrapper getMarketInfo() throws IOException;

  @GET
  @Path("pairs")
  ExxCurrencyPairs getPairs() throws IOException;

  @GET
  @Path("orderBooks")
  Map<String, ExxDepth> getDepths() throws IOException;

  @GET
  @Path("tickers")
  Map<String, ExxTicker> getTickers() throws IOException;

  @GET
  @Path("ticker/{ident}_{currency}")
  ExxTicker getTicker(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency) throws IOException;

  @GET
  @Path("depth/{ident}_{currency}")
  ExxDepth getFullDepth(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency) throws IOException;

  @GET
  @Path("trade/{ident}_{currency}")
  ExxTradeHistory getTradeHistory(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency) throws IOException;

  @GET
  @Path("trade/{ident}_{currency}/{tradeId}")
  ExxTradeHistory getTradeHistorySince(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency,
      @PathParam("tradeId") String tradeId) throws IOException;
}
