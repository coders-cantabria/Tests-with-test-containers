package com.salpreh.products.driving.consumer.adapters;

import com.salpreh.products.driving.consumer.models.ExternalPalletUnloadEvent;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import com.salpreh.products.tests.base.BaseDynamicPropsITTest;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class PalletUnloadKafkaConsumerITTest extends BaseDynamicPropsITTest {

  @Autowired
  KafkaTemplate<String, ExternalPalletUnloadEvent> kafkaTemplate;

  private KafkaConsumer<String, ExternalStockUpdateEvent> consumer;

  // TODO: You might need to add more dependencies

  @BeforeEach
  void setUp() {
     // TODO: Tests setup
  }

  @AfterEach
  void tearDown() {
    if (consumer != null) consumer.close();
  }

  @Test
  void givenPalletUnload_whenConsumed_shouldCreatePalletAndUpdateStock() {
    // given
    String palletId = "123456768912349";
    String productCode = "00000000000001";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenPalletUnload_whenConsumedAndPalletExists_shouldNotCreateNorUpdateStock() {
    // given
    String palletId = "123456768912349";
    String productCode = "00000000000001";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenPalletUnload_whenConsumedAndProductDoNotExists_shouldNotCreateNorUpdateStock() {
    // given
    String palletId = "123456768912349";
    String productCode = "00000000000009";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }


  private ExternalPalletUnloadEvent createPalletUnloadEvent(String id, String productId, Long storeId, int quantity) {
    return ExternalPalletUnloadEvent.builder()
      .globalId(id)
      .destinationId(storeId)
      .productId(productId)
      .units(quantity)
      .build();
  }
}
