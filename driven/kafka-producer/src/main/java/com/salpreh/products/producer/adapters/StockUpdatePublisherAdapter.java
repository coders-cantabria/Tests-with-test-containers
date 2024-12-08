package com.salpreh.products.producer.adapters;

import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.application.ports.driving.StockUpdatePublisherPort;
import com.salpreh.products.producer.mappers.PublisherMapper;
import com.salpreh.products.producer.models.ExternalStockUpdateEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockUpdatePublisherAdapter implements StockUpdatePublisherPort {

  private final String topic;
  private final String systemId;
  private final PublisherMapper mapper;
  private final KafkaTemplate<String, ExternalStockUpdateEvent> kafkaTemplate;

  public StockUpdatePublisherAdapter(
    @Value("${kafka.topics.stock-update.topic}") String topic,
    @Value("${products-app.system-id}") String systemId,
    PublisherMapper mapper,
    KafkaTemplate<String, ExternalStockUpdateEvent> kafkaTemplate
  ) {
    this.topic = topic;
    this.systemId = systemId;
    this.mapper = mapper;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void publishStockUpdate(StockUpdateEvent stockUpdate) {
    ExternalStockUpdateEvent externalStockUpdate = mapper.toExternalEvent(stockUpdate);
    externalStockUpdate.setOriginSystemId(systemId);

    kafkaTemplate.send(topic, systemId, externalStockUpdate);
  }
}
