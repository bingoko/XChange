package org.knowm.xchange.exx;

import java.io.IOException;
import java.util.Map;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper.exxMarketInfo;
import org.knowm.xchange.exx.service.ExxAccountService;
import org.knowm.xchange.exx.service.ExxMarketDataService;
import org.knowm.xchange.exx.service.ExxMarketDataServiceRaw;
import org.knowm.xchange.exx.service.ExxTradeService;
import org.knowm.xchange.utils.nonce.AtomicLongIncrementalTime2013NonceFactory;

import si.mazi.rescu.SynchronizedValueFactory;

public class ExxExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory = new AtomicLongIncrementalTime2013NonceFactory();

  @Override
  protected void initServices() {
    this.marketDataService = new ExxMarketDataService(this);
    this.accountService = new ExxAccountService(this);
    this.tradeService = new ExxTradeService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://www.exx.com/");
    exchangeSpecification.setHost("exx.com");
    exchangeSpecification.setExchangeName("EXX.com");

    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }

  @Override
  public void remoteInit() throws IOException {

    Map<CurrencyPair, exxMarketInfo> currencyPair2BTERMarketInfoMap = ((ExxMarketDataServiceRaw) marketDataService).getBTERMarketInfo();
    exchangeMetaData = ExxAdapters.adaptToExchangeMetaData(currencyPair2BTERMarketInfoMap);
  }
}
