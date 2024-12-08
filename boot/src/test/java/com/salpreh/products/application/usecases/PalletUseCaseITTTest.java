package com.salpreh.products.application.usecases;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.exceptions.EanProcessingException;
import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.application.ports.driven.PalletRepositoryPort;
import com.salpreh.products.application.ports.driven.StoreStockRepositoryPort;
import com.salpreh.products.application.ports.driving.PalletUseCasePort;
import com.salpreh.products.tests.base.DbSpringbootITTest;
import com.salpreh.products.tests.utils.Scripts;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

public class PalletUseCaseITTTest extends DbSpringbootITTest {

  @Autowired
  private PalletUseCasePort palletUseCase;

  @Autowired
  private StoreStockRepositoryPort storeStockRepository;

  @Autowired
  private PalletRepositoryPort palletRepository;

  @BeforeEach
  void setUp() {
    loadData(Scripts.DELETE_ALL);
    loadData(Scripts.DATA_ALL);
  }

  @Test
  void givenEanCode_whenCreate_shouldReturnPallet() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000002 | StoreID: 0000000003001 | Quantity: 35
    String eanCode = "0012345678912345678901000000000000011012*11240101412000000000000241000000000030013735*";
    Long storeId = 3001L;
    String productCode = "00000000000001";
    int quantity = 35;

    StoreStock previousStock = storeStockRepository.findById(storeId, productCode)
      .orElseThrow();

    // when
    Pallet pallet = palletUseCase.createPallet(eanCode);

    // then
    await()
      .pollDelay(Duration.ofSeconds(1))
      .untilAsserted(() -> {
        StoreStock currentStock = storeStockRepository.findById(storeId, productCode)
          .orElseThrow();
        assertEquals(previousStock.getQuantity() + quantity, currentStock.getQuantity());
      });

    assertTrue(palletRepository.existsById(pallet.getId()));
    assertEquals(productCode, pallet.getProduct().getId());
    assertNotNull(pallet.getProduct().getName());
    assertEquals(storeId, pallet.getStore().getId());
    assertNotNull(pallet.getStore().getName());
    assertEquals(quantity, pallet.getUnits());
  }

  @Test
  void givenEanCode_whenDecode_shouldReturnPallet() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000002 | StoreID: 0000000003001 | Quantity: 35
    String eanCode = "0012345678912345678901000000000000011012*11240101412000000000000241000000000030013735*";

    // when
    Pallet pallet = palletUseCase.decodeEan128(eanCode);

    // then
    assertEquals("123456789123456789", pallet.getId());
    assertEquals("00000000000001", pallet.getProduct().getId());
    assertNotNull(pallet.getProduct().getName());
    assertEquals("12", pallet.getBatchId());
    assertEquals("2024-01-01", pallet.getProductionDate().toString());
    assertEquals(2L, pallet.getSupplier().getId());
    assertNotNull(pallet.getSupplier().getName());
    assertEquals(3001, pallet.getStore().getId());
    assertNotNull(pallet.getStore().getName());
    assertEquals(35, pallet.getUnits());
  }

  @Test
  void givenEanCode_whenDecodeAndMissingRequiredCodes_shouldThrowException() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID: None | StoreID: 0000000003001 | Quantity: None
    // Missing required IA
    String eanCode = "0012345678912345678901000000000000011012*112401014100000000003001";

    // when
    Executable decode = () -> palletUseCase.decodeEan128(eanCode);

    // then
    EanProcessingException exception = Assertions.assertThrows(EanProcessingException.class, decode);
    assertTrue(exception.getMessage().contains("Missing required"));
  }

  @Test
  void givenEanCode_whenDecodeAndUnknownCode_shouldThrowException() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID: None | StoreID: 0000000003001 | Quantity: None | Unknown 55: 123
    // Unknown IA 55
    String eanCode = "0012345678912345678901000000000000011012*11240101410000000000300155123";

    // when
    Executable decode = () -> palletUseCase.decodeEan128(eanCode);

    // then
    EanProcessingException exception = Assertions.assertThrows(EanProcessingException.class, decode);
    assertTrue(exception.getMessage().contains("unknown IA"));
  }

  @Test
  void givenEanCode_whenDecodeAndSupplierDoNotExists_shouldThrowException() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000009 | StoreID: 0000000003001 | Quantity: 35
    // Nonexistent supplier
    String eanCode = "0012345678912345678901000000000000011012*11240101412000000000000941000000000030013735*";

    // when
    Executable decode = () -> palletUseCase.decodeEan128(eanCode);

    // then
    EanProcessingException exception = Assertions.assertThrows(EanProcessingException.class, decode);
    assertTrue(exception.getMessage().contains("supplier id"));
  }

  @Test
  void givenEanCode_whenDecodeAndStoreDoNotExists_shouldThrowException() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000002 | StoreID: 0000000003009 | Quantity: 35
    // Nonexistent store
    String eanCode = "0012345678912345678901000000000000011012*11240101412000000000000241000000000030913735*";

    // when
    Executable decode = () -> palletUseCase.decodeEan128(eanCode);

    // then
    EanProcessingException exception = Assertions.assertThrows(EanProcessingException.class, decode);
    assertTrue(exception.getMessage().contains("store id"));
  }

  @Test
  void givenEanCode_whenDecodeAndProductDoNotExists_shouldThrowException() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000009 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000002 | StoreID: 0000000003001 | Quantity: 35
    // Nonexistent product
    String eanCode = "0012345678912345678901000000000000091012*11240101412000000000000241000000000030013735*";

    // when
    Executable decode = () -> palletUseCase.decodeEan128(eanCode);

    // then
    EanProcessingException exception = Assertions.assertThrows(EanProcessingException.class, decode);
    assertTrue(exception.getMessage().contains("product id"));
  }
}
