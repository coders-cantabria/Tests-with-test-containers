package com.salpreh.products.config.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Deserializer created for testing purposes. It decorates {@link StringDeserializer} with additional JSON deserialization.
 * @param <T>
 */
public class JsonStringDeserializer<T> implements Deserializer<T> {

  private final StringDeserializer stringDeserializer = new StringDeserializer();
  private final Class<T> clazz;
  private final ObjectMapper objectMapper;

  public JsonStringDeserializer(Class<T> clazz, ObjectMapper objectMapper) {
    this.clazz = clazz;
    this.objectMapper = objectMapper;
  }

  @Override
  public T deserialize(String s, byte[] bytes) {
    String jsonData = stringDeserializer.deserialize(s, bytes);
    try {
      return objectMapper.readValue(jsonData, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public T deserialize(String topic, Headers headers, byte[] data) {
    String jsonData = stringDeserializer.deserialize(topic, headers, data);
    try {
      return objectMapper.readValue(jsonData, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public T deserialize(String topic, Headers headers, ByteBuffer data) {
    String jsonData = stringDeserializer.deserialize(topic, headers, data);
    try {
      return objectMapper.readValue(jsonData, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
