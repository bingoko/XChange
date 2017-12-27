package org.knowm.xchange.exx.dto;

import java.io.IOException;

import org.knowm.xchange.exx.dto.ExxOrderType.BTEROrderTypeDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BTEROrderTypeDeserializer.class)
public enum ExxOrderType {

  BUY, SELL;

  static class BTEROrderTypeDeserializer extends JsonDeserializer<ExxOrderType> {

    @Override
    public ExxOrderType deserialize(JsonParser jsonParser, final DeserializationContext ctxt) throws IOException, JsonProcessingException {

      final ObjectCodec oc = jsonParser.getCodec();
      final JsonNode node = oc.readTree(jsonParser);
      final String orderType = node.asText();
      return ExxOrderType.valueOf(orderType.toUpperCase());
    }
  }
}
