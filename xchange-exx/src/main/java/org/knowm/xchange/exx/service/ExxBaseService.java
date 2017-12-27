package org.knowm.xchange.exx.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exx.ExxAuthenticated;
import org.knowm.xchange.exx.dto.ExxBaseResponse;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

public class ExxBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final ExxAuthenticated bter;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  public ExxBaseService(Exchange exchange) {

    super(exchange);

    this.bter = RestProxyFactory.createProxy(ExxAuthenticated.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = ExxHmacPostBodyDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }

  protected <R extends ExxBaseResponse> R handleResponse(R response) {

    if (!response.isResult()) {
      throw new ExchangeException(response.getMessage());
    }

    return response;
  }

}
