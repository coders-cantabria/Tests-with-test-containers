package com.salpreh.products.application.usecases;

import com.salpreh.products.tests.base.DbSpringbootITTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PalletUseCaseITTTest extends DbSpringbootITTest {

  @BeforeEach
  void setUp() {
    // TODO: Prepare tests setup
  }

  @Test
  void givenEanCode_whenCreate_shouldReturnPallet() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000002 | StoreID: 0000000003001 | Quantity: 35
    String eanCode = "0012345678912345678901000000000000011012*11240101412000000000000241000000000030013735*";
    String palletId = "123456789123456789";
    Long storeId = 3001L;
    String productCode = "00000000000001";
    int quantity = 35;

    // when

    // then
  }

  @Test
  void givenEanCode_whenDecode_shouldReturnPallet() {
    // given
    // ID: 123456789123456789 | ProdID: 00000000000001 | Batch: 12 | ProdDate: 2024-01-01 | SupplID:  0000000000002 | StoreID: 0000000003001 | Quantity: 35
    String eanCode = "0012345678912345678901000000000000011012*11240101412000000000000241000000000030013735*";
    String palletId = "123456789123456789";
    Long storeId = 3001L;
    String productCode = "00000000000001";
    int quantity = 35;

    // when

    // then
  }
}
