package com.salpreh.products.driving.consumer.consumers;

import com.salpreh.products.driving.consumer.models.ExternalPalletUnloadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExternalPalletUnloadKafkaConsumer {

  @KafkaListener(
    topics = "${kafka.topics.pallet-unload.topic}",
    groupId = "${kafka.topics.pallet-unload.group-id}"
  )
  public void consumeEvent(
    @Header(KafkaHeaders.RECEIVED_KEY) String key,
    @Payload ExternalPalletUnloadEvent payload
  ) {
    log.info("External pallet unload from supplier {}: {}", key, payload);
  }
}
