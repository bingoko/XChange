package org.knowm.xchange.exx.service;

import java.io.IOException;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.exx.dto.account.ExxFunds;

public class ExxAccountServiceRaw extends ExxBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public ExxAccountServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public ExxFunds getExxAccountInfo() throws IOException {

    ExxFunds exxFunds = bter.getFunds(exchange.getExchangeSpecification().getApiKey(), signatureCreator, exchange.getNonceFactory());
    return handleResponse(exxFunds);
  }

}
