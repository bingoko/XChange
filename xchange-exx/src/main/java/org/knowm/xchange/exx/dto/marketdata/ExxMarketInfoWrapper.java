package org.knowm.xchange.exx.dto.marketdata;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exx.ExxAdapters;
import org.knowm.xchange.exx.dto.marketdata.ExxMarketInfoWrapper.BTERMarketInfoWrapperDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BTERMarketInfoWrapperDeserializer.class)
public class ExxMarketInfoWrapper {

  private final Map<CurrencyPair, exxMarketInfo> marketInfoMap;

  private ExxMarketInfoWrapper(Map<CurrencyPair, exxMarketInfo> marketInfoMap) {

    this.marketInfoMap = marketInfoMap;
  }

  public Map<CurrencyPair, exxMarketInfo> getMarketInfoMap() {

    return marketInfoMap;
  }

  @Override
  public String toString() {

    return "GateioMarketInfoWrapper [marketInfoMap=" + marketInfoMap + "]";
  }

  public static class exxMarketInfo {

    private final CurrencyPair currencyPair;
    private final int decimalPlaces;
    private final BigDecimal minAmount;
    private final BigDecimal fee;

    public exxMarketInfo(CurrencyPair currencyPair, int decimalPlaces, BigDecimal minAmount, BigDecimal fee) {

      this.currencyPair = currencyPair;
      this.decimalPlaces = decimalPlaces;
      this.minAmount = minAmount;
      this.fee = fee;
    }

    public CurrencyPair getCurrencyPair() {

      return currencyPair;
    }

    public int getDecimalPlaces() {

      return decimalPlaces;
    }

    public BigDecimal getMinAmount() {

      return minAmount;
    }

    public BigDecimal getFee() {

      return fee;
    }

    @Override
    public String toString() {

      return "BTERMarketInfo [currencyPair=" + currencyPair + ", decimalPlaces=" + decimalPlaces + ", minAmount=" + minAmount + ", fee=" + fee + "]";
    }

  }

  static class BTERMarketInfoWrapperDeserializer extends JsonDeserializer<ExxMarketInfoWrapper> {

    @Override
    public ExxMarketInfoWrapper deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

      Map<CurrencyPair, exxMarketInfo> marketInfoMap = new HashMap<>();

      ObjectCodec oc = jp.getCodec();
      JsonNode marketsNodeWrapper = oc.readTree(jp);
      JsonNode marketNodeList = marketsNodeWrapper.path("pairs");

      if (marketNodeList.isArray()) {
        for (JsonNode marketNode : marketNodeList) {
          Iterator<Map.Entry<String, JsonNode>> iter = marketNode.fields();
          if (iter.hasNext()) {
            Entry<String, JsonNode> entry = iter.next();
            CurrencyPair currencyPair = ExxAdapters.adaptCurrencyPair(entry.getKey());
            JsonNode marketInfoData = entry.getValue();
            int decimalPlaces = marketInfoData.path("decimal_places").asInt();
            BigDecimal minAmount = new BigDecimal(marketInfoData.path("min_amount").asText());
            BigDecimal fee = new BigDecimal(marketInfoData.path("fee").asText());
            exxMarketInfo marketInfoObject = new exxMarketInfo(currencyPair, decimalPlaces, minAmount, fee);

            marketInfoMap.put(currencyPair, marketInfoObject);
          } else {
            throw new ExchangeException("Invalid market info response received from Gateio." + marketsNodeWrapper);
          }
        }
      } else {
        throw new ExchangeException("Invalid market info response received from Gateio." + marketsNodeWrapper);
      }

      return new ExxMarketInfoWrapper(marketInfoMap);
    }
  }
}
