package com.salpreh.products.driving.consumer.consumers;

import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.application.ports.driving.StockUpdateUseCasePort;
import com.salpreh.products.driving.consumer.mappers.EventMapper;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdateKafkaConsumer {

  private final StockUpdateUseCasePort stockUpdateUseCase;
  private final EventMapper mapper;

  @KafkaListener(
    topics = "${kafka.topics.stock-update.topic}",
    groupId = "${kafka.topics.stock-update.group-id}",
    containerFactory = "kafkaJsonListenerContainerFactory"
  )
  public void consumeEvent(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload ExternalStockUpdateEvent payload) {
    log.info("Stock update event from system {}: {}", key, payload);

    StockUpdateEvent stockUpdate = mapper.toDomain(payload);
    stockUpdateUseCase.processStockUpdate(stockUpdate);

    log.info("Stock update event from system {} processed", key);
  }
}
