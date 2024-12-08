package com.salpreh.products.driving.consumer.adapters;

import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import com.salpreh.products.tests.base.BaseDynamicPropsITTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.kafka.core.KafkaTemplate;

public class StockUpdateKafkaConsumerITTest extends BaseDynamicPropsITTest {

  private static final String ORIGIN_SYSTEM_ID = "system1";

  @Autowired
  private KafkaTemplate<String, ExternalStockUpdateEvent> kafkaTemplate;

  // TODO: You might need to add more dependencies

  @BeforeEach
  void setUp() {
    // TODO: Tests setup
  }

  @Test
  void givenStockUpdateEvent_whenConsumedAndNoStockInitialized_shouldCreateStock() {
    // given
    String productCode = "00000000000001";
    Long storeId = 3002L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenStockUpdateEvent_whenConsumed_shouldUpdateStock() {
    // given
    String productCode = "00000000000001";
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenStockUpdateEvent_whenConsumedAndProductDoNotExists_shouldNotUpdateStock(CapturedOutput capturedOutput) {
    // given
    String productCode = "00000000000009";
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenStockUpdateEvent_whenConsumedAndStoreDoNotExists_shouldNotUpdateStock(CapturedOutput capturedOutput) {
    // given
    String productCode = "00000000000001";
    Long storeId = 3009L;
    int quantity = 10;

    // when

    // then
  }

  private ExternalStockUpdateEvent createStockUpdateEvent(String productId, Long storeId, int quantity) {
    return ExternalStockUpdateEvent.builder()
      .originSystemId(ORIGIN_SYSTEM_ID)
      .productId(productId)
      .storeId(storeId)
      .quantity(quantity)
      .build();
  }
}
