package com.salpreh.products.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {
  @Bean
  public KafkaListenerContainerFactory<?> kafkaJsonListenerContainerFactory(ConsumerFactory<String, String> consumerFactory, ObjectMapper objectMapper) {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
      new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setRecordMessageConverter(new JsonMessageConverter(objectMapper));

    return factory;
  }

  @Bean
  public <T> DefaultKafkaProducerFactory<String, T> kafkaJsonProducerFactory(ObjectMapper objectMapper, KafkaProperties properties) {
    DefaultKafkaProducerFactory<String, T> producerFactory = new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(null));
    Serializer<T> jsonSerializer = jsonSerializer(objectMapper);
    producerFactory.setValueSerializer(jsonSerializer);

    return producerFactory;
  }

  public <T> JsonSerializer<T> jsonSerializer(ObjectMapper objectMapper) {
    JsonSerializer<T> serializer = new JsonSerializer<>(objectMapper);

    return serializer;
  }
}
