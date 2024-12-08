package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.commons.IdName;
import com.salpreh.products.driving.consumer.adapters.StockUpdateKafkaConsumer;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import com.salpreh.products.tests.base.BaseDynamicPropsITTest;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class PalletUseCaseITTest extends BaseDynamicPropsITTest {

  // Just to avoid consuming tested event publication
  @MockBean
  private StockUpdateKafkaConsumer stockUpdateKafkaConsumer;

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
  void givenPallet_whenCreate_shouldSaveAndPublishEvent() {
    // given
    String palletId = "123456768912349";
    String productId = "00000000000001";
    Long supplierId = 2L;
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenPallet_whenCreateAndProductDoNotExists_shouldThrowException() {
    // given
    String palletId = "123456768912349";
    String productId = "00000000000009";
    Long supplierId = 2L;
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
  }

  @Test
  void givenPallet_whenCreateAndPalletExists_shouldNotSaveNorPublishEvent() {
    // given
    String palletId = "123456768912349";
    String productId = "00000000000001";
    Long supplierId = 1L;
    Long storeId = 3001L;
    int quantity = 10;

    // when

    // then
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
