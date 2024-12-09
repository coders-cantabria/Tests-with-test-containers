package com.salpreh.products.driving.consumer.adapters;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.application.ports.driven.PalletRepositoryPort;
import com.salpreh.products.application.ports.driven.StoreStockRepositoryPort;
import com.salpreh.products.driving.consumer.models.ExternalPalletUnloadEvent;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import com.salpreh.products.tests.base.BaseDynamicPropsITTest;
import com.salpreh.products.tests.utils.Scripts;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class PalletUnloadKafkaConsumerITTest extends BaseDynamicPropsITTest {

  @Value("${kafka.topics.pallet-unload.topic}")
  private String palletUnloadTopic;
  @Value("${kafka.topics.stock-update.topic}")
  private String stockUpdateTopic;

  @Autowired
  KafkaTemplate<String, ExternalPalletUnloadEvent> kafkaTemplate;

  @Autowired
  private PalletRepositoryPort palletRepository;

  @Autowired
  private StoreStockRepositoryPort stockRepository;

  private KafkaConsumer<String, ExternalStockUpdateEvent> consumer;

  @BeforeEach
  void setUp() {
     loadData(Scripts.DELETE_ALL);
     deleteTopics(List.of(palletUnloadTopic, stockUpdateTopic));

     loadData(Scripts.DATA_ALL);
     createTopics(List.of(palletUnloadTopic, stockUpdateTopic));
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
    ExternalPalletUnloadEvent event = createPalletUnloadEvent(palletId, productCode, storeId, quantity);

    StoreStock previousStock = stockRepository.findById(storeId, productCode)
      .orElseThrow();

    consumer = createConsumer(ExternalStockUpdateEvent.class);
    consumer.subscribe(List.of(stockUpdateTopic));

    // when
    kafkaTemplate.send(palletUnloadTopic, String.valueOf(supplierId), event);

    // then
    await()
      .untilAsserted(() -> {
        Optional<Pallet> pallet = palletRepository.findById(palletId);
        assertTrue(pallet.isPresent());

        Pallet palletData = pallet.get();
        assertEquals(palletId, palletData.getId());
        assertEquals(productCode, palletData.getProduct().getId());
        assertEquals(supplierId, palletData.getSupplier().getId());
        assertEquals(storeId, palletData.getStore().getId());
        assertEquals(quantity, palletData.getUnits());
      });

    await()
      .untilAsserted(() -> {
        ConsumerRecords<String, ExternalStockUpdateEvent> records = consumer.poll(Duration.ofMillis(200));
        assertEquals(1, records.count());
      });

    await()
      .untilAsserted(() -> {
        StoreStock currentStock = stockRepository.findById(storeId, productCode)
          .orElseThrow();
        assertEquals(previousStock.getQuantity() + quantity, currentStock.getQuantity());
      });
  }

  @Test
  void givenPalletUnload_whenConsumedAndPalletExists_shouldNotCreateNorUpdateStock() {
    // given
    String palletId = "123456768912349";
    String productCode = "00000000000001";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;
    ExternalPalletUnloadEvent event = createPalletUnloadEvent(palletId, productCode, storeId, quantity);

    loadData(Scripts.PALLET_CREATE);
    StoreStock previousStock = stockRepository.findById(storeId, productCode)
      .orElseThrow();

    consumer = createConsumer(ExternalStockUpdateEvent.class);
    consumer.subscribe(List.of(stockUpdateTopic));

    // when
    kafkaTemplate.send(palletUnloadTopic, String.valueOf(supplierId), event);

    // then
    await()
      .untilAsserted(() -> {
        Optional<Pallet> palletData = palletRepository.findById(palletId);
        assertTrue(palletData.isPresent());
        assertNotEquals(quantity, palletData.get().getUnits());
      });

    await()
      .during(Duration.ofSeconds(3))
      .until(() -> consumer.poll(Duration.ofMillis(200)).count() == 0);

    StoreStock currentStock = stockRepository.findById(storeId, productCode)
      .orElseThrow();
    assertEquals(previousStock.getQuantity(), currentStock.getQuantity());
  }

  @Test
  void givenPalletUnload_whenConsumedAndProductDoNotExists_shouldNotCreateNorUpdateStock() {
    // given
    String palletId = "123456768912349";
    String productCode = "00000000000009";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;
    ExternalPalletUnloadEvent event = createPalletUnloadEvent(palletId, productCode, storeId, quantity);

    consumer = createConsumer(ExternalStockUpdateEvent.class);
    consumer.subscribe(List.of(stockUpdateTopic));

    // when
    kafkaTemplate.send(palletUnloadTopic, String.valueOf(supplierId), event);

    // then
    await()
      .during(Duration.ofSeconds(5))
      .until(() -> !palletRepository.existsById(palletId));

    await()
      .during(Duration.ofSeconds(1))
      .until(() -> consumer.poll(Duration.ofMillis(200)).count() == 0);

    assertTrue(stockRepository.findById(storeId, productCode).isEmpty());
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
