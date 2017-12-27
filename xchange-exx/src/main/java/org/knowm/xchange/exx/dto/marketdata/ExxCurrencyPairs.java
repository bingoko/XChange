package org.knowm.xchange.exx.dto.marketdata;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exx.ExxAdapters;
import org.knowm.xchange.exx.dto.marketdata.ExxCurrencyPairs.BTERCurrencyPairsDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BTERCurrencyPairsDeserializer.class)
public class ExxCurrencyPairs {

  private final Set<CurrencyPair> pairs;

  private ExxCurrencyPairs(Set<CurrencyPair> pairs) {

    this.pairs = pairs;
  }

  public Collection<CurrencyPair> getPairs() {

    return pairs;
  }

  @Override
  public String toString() {

    return "GateioCurrencyPairs [pairs=" + pairs + "]";
  }

  static class BTERCurrencyPairsDeserializer extends JsonDeserializer<ExxCurrencyPairs> {

    @Override
    public ExxCurrencyPairs deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

      final Set<CurrencyPair> pairs = new HashSet<>();
      final ObjectCodec oc = jp.getCodec();
      final JsonNode node = oc.readTree(jp);
      if (node.isArray()) {
        for (JsonNode pairNode : node) {
          pairs.add(ExxAdapters.adaptCurrencyPair(pairNode.asText()));
        }
      }
      return new ExxCurrencyPairs(pairs);
    }
  }
}
