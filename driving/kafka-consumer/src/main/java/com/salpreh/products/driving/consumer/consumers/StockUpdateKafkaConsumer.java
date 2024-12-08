package com.salpreh.products.driving.consumer.consumers;

import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockUpdateKafkaConsumer {


  @KafkaListener(
    topics = "${kafka.topics.stock-update.topic}",
    groupId = "${kafka.topics.stock-update.group-id}"
  )
  public void consumeEvent(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload ExternalStockUpdateEvent payload) {
    log.info("Stock update event from system {}: {}", key, payload);
  }
}
