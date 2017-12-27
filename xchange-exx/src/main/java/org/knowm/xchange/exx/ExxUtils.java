package org.knowm.xchange.exx;

import org.knowm.xchange.currency.CurrencyPair;

public class ExxUtils {

  public static String toPairString(CurrencyPair currencyPair) {

    String baseSymbol = currencyPair.base.getCurrencyCode().toLowerCase();
    String counterSymbol = currencyPair.counter.getCurrencyCode().toLowerCase();
    String pair = baseSymbol + "_" + counterSymbol;

    return pair;
  }
}
