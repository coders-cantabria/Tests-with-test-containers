package com.salpreh.products.application.usecases;

import static com.salpreh.products.tests.utils.Scripts.PALLET_CREATE;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.commons.IdName;
import com.salpreh.products.application.ports.driven.PalletRepositoryPort;
import com.salpreh.products.application.ports.driving.PalletUseCasePort;
import com.salpreh.products.driving.consumer.adapters.StockUpdateKafkaConsumer;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import com.salpreh.products.tests.base.BaseDynamicPropsITTest;
import com.salpreh.products.tests.utils.Scripts;
import java.time.Duration;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

public class PalletUseCaseITTest extends BaseDynamicPropsITTest {

  // Just to avoid consuming tested event publication
  @MockBean
  private StockUpdateKafkaConsumer stockUpdateKafkaConsumer;

  @Value("${kafka.topics.stock-update.topic}")
  public String topic;

  @Autowired
  private PalletUseCasePort palletUseCase;

  @Autowired
  private PalletRepositoryPort palletRepository;

  private KafkaConsumer<String, ExternalStockUpdateEvent> consumer;

  @BeforeEach
  void setUp() {
    loadData(Scripts.DELETE_ALL);
    deleteTopics(List.of(topic));

    loadData(Scripts.DATA_ALL);
    createTopics(List.of(topic));
  }

  @AfterEach
  void tearDown() {
    if (consumer != null) consumer.close();
  }

  @Test
  void givenPallet_whenCreate_shouldSaveAndPublishEvent() {
    // given
    String palletId = "123456768912349";
    String productId = "00000000000001";
    Long supplierId = 2L;
    Long storeId = 3001L;
    int quantity = 10;
    Pallet pallet = createPallet(palletId, productId, supplierId, storeId, quantity);

    consumer = createConsumer(ExternalStockUpdateEvent.class);
    consumer.subscribe(List.of(topic));

    // when
    Pallet createdPallet = palletUseCase.createPallet(pallet);

    // then
    await()
      .untilAsserted(() -> {
        ConsumerRecords<String, ExternalStockUpdateEvent> records = consumer.poll(Duration.ofMillis(100));
        assertEquals(1, records.count());

        ExternalStockUpdateEvent event = records.iterator().next().value();
        assertEquals(productId, event.getProductId());
        assertEquals(storeId, event.getStoreId());
        assertEquals(quantity, event.getQuantity());
      });

    assertTrue(palletRepository.existsById(createdPallet.getId()));
    assertEquals(palletId, createdPallet.getId());
    assertEquals(productId, createdPallet.getProduct().getId());
    assertEquals(supplierId, createdPallet.getSupplier().getId());
    assertEquals(storeId, createdPallet.getStore().getId());
    assertEquals(quantity, createdPallet.getUnits());
  }

  @Test
  void givenPallet_whenCreateAndProductDoNotExists_shouldThrowException() {
    // given
    String palletId = "123456768912349";
    String productId = "00000000000009";
    Long supplierId = 2L;
    Long storeId = 3001L;
    int quantity = 10;
    Pallet pallet = createPallet(palletId, productId, supplierId, storeId, quantity);

    consumer = createConsumer(ExternalStockUpdateEvent.class);
    consumer.subscribe(List.of(topic));

    // when
    Executable create = () -> palletUseCase.createPallet(pallet);

    // then
    assertThrows(IllegalArgumentException.class, create);

    await()
      .during(Duration.ofSeconds(3))
      .until(() -> consumer.poll(Duration.ofMillis(400)).count() == 0);
  }

  @Test
  void givenPallet_whenCreateAndPalletExists_shouldNotSaveNorPublishEvent() {
    // given
    String palletId = "123456768912349";
    String productId = "00000000000001";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;
    Pallet pallet = createPallet(palletId, productId, supplierId, storeId, quantity);

    loadData(PALLET_CREATE);

    consumer = createConsumer(ExternalStockUpdateEvent.class);
    consumer.subscribe(List.of(topic));

    // when
    Pallet createdPallet = palletUseCase.createPallet(pallet);

    // then
    assertTrue(palletRepository.existsById(createdPallet.getId()));
    assertNotEquals(quantity, createdPallet.getUnits());

    await()
      .during(Duration.ofSeconds(3))
      .until(() -> consumer.poll(Duration.ofMillis(400)).count() == 0);
  }

  private Pallet createPallet(String id, String productId, Long supplierId, Long storeId, int quantity) {
    return Pallet.builder()
      .id(id)
      .store(IdName.of(storeId, "storename"))
      .supplier(IdName.of(supplierId, "suppliername"))
      .product(IdName.of(productId, "productname"))
      .units(quantity)
      .build();
  }
}
