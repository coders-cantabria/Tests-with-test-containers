package com.salpreh.products.driving.consumer.adapters;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.application.ports.driven.StoreStockRepositoryPort;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import com.salpreh.products.tests.base.BaseDynamicPropsITTest;
import com.salpreh.products.tests.utils.Scripts;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;

public class StockUpdateKafkaConsumerITTest extends BaseDynamicPropsITTest {

  private static final String ORIGIN_SYSTEM_ID = "system1";

  @Value("${kafka.topics.stock-update.topic}")
  private String topic;

  @Autowired
  private KafkaTemplate<String, ExternalStockUpdateEvent> kafkaTemplate;

  @Autowired
  private StoreStockRepositoryPort storeStockRepository;

  @BeforeEach
  void setUp() {
    loadData(Scripts.DELETE_ALL);
    deleteTopics(List.of(topic));

    loadData(Scripts.DATA_ALL);
    createTopics(List.of(topic));
  }

  @Test
  void givenStockUpdateEvent_whenConsumedAndNoStockInitialized_shouldCreateStock() {
    // given
    String productCode = "00000000000001";
    Long storeId = 3002L;
    int quantity = 10;
    ExternalStockUpdateEvent event = createStockUpdateEvent(productCode, storeId, quantity);

    Optional<StoreStock> previousStock = storeStockRepository.findById(storeId, productCode);

    // when
    kafkaTemplate.send(topic, ORIGIN_SYSTEM_ID, event);

    // then
    await()
      .untilAsserted(() -> {
        Optional<StoreStock> currentStock = storeStockRepository.findById(storeId, productCode);

        assertTrue(currentStock.isPresent());
        assertTrue(previousStock.isEmpty());
        assertEquals(quantity, currentStock.get().getQuantity());
      });
  }

  @Test
  void givenStockUpdateEvent_whenConsumed_shouldUpdateStock() {
    // given
    String productCode = "00000000000001";
    Long storeId = 3001L;
    int quantity = 10;
    ExternalStockUpdateEvent event = createStockUpdateEvent(productCode, storeId, quantity);

    StoreStock previousStock = storeStockRepository.findById(storeId, productCode)
      .orElseThrow();

    // when
    kafkaTemplate.send(topic, ORIGIN_SYSTEM_ID, event);

    // then
    await()
      .untilAsserted(() -> {
        StoreStock currentStock = storeStockRepository.findById(storeId, productCode)
          .orElseThrow();
        assertEquals(previousStock.getQuantity() + quantity, currentStock.getQuantity());
      });
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void givenStockUpdateEvent_whenConsumedAndProductDoNotExists_shouldNotUpdateStock(CapturedOutput capturedOutput) {
    // given
    String productCode = "00000000000009";
    Long storeId = 3001L;
    int quantity = 10;
    ExternalStockUpdateEvent event = createStockUpdateEvent(productCode, storeId, quantity);

    // when
    kafkaTemplate.send(topic, ORIGIN_SYSTEM_ID, event);

    // then
    await()
      .untilAsserted(() -> {
        assertTrue(capturedOutput.getOut().contains("non existing product"));

        Optional<StoreStock> currentStock = storeStockRepository.findById(storeId, productCode);
        assertTrue(currentStock.isEmpty());
      });
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void givenStockUpdateEvent_whenConsumedAndStoreDoNotExists_shouldNotUpdateStock(CapturedOutput capturedOutput) {
    // given
    String productCode = "00000000000001";
    Long storeId = 3009L;
    int quantity = 10;
    ExternalStockUpdateEvent event = createStockUpdateEvent(productCode, storeId, quantity);

    // when
    kafkaTemplate.send(topic, ORIGIN_SYSTEM_ID, event);

    // then
    await()
      .untilAsserted(() -> {
        assertTrue(capturedOutput.getOut().contains("non existing store"));

        Optional<StoreStock> currentStock = storeStockRepository.findById(storeId, productCode);
        assertTrue(currentStock.isEmpty());
      });
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
