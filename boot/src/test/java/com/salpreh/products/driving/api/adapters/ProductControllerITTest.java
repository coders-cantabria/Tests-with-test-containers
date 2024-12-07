package com.salpreh.products.driving.api.adapters;

import com.salpreh.products.tests.base.DbSpringbootITTest;
import com.salpreh.products.tests.utils.ApiData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerITTest extends DbSpringbootITTest {

  @LocalServerPort
  private Integer port;

  @BeforeEach
  void setUp() {
    // TODO: Load data and whatever you think is necessary
  }

  @Test
  void givenProducts_whenGetAllProducts_shouldReturnAll() {
    // given
    int page = 0;
    int size = 100;

    // when

    // then
  }

  @Test
  void givenProducts_whenGetAllWithFilters_shouldReturnFiltered() {
    // given
    int page = 0;
    int size = 100;
    String tags = "lacteos,sin gluten";
    String purchasePriceRange = "1.0,2.0";
    String supplierIds = "3";

    // when

    // then
    // TODO: Returns product 3, 4 and 5
  }

  @Test
  void givenProduct_whenGetByBarcode_shouldReturnProduct() {
    // given
    String barcode = "00000000000001";

    // when

    // then
  }

  @Test
  void givenProduct_whenGetByBarcodeAndDoNotExists_shouldReturnNotFound() {
    // given
    String barcode = "00000000000099";

    // when

    // then
  }

  @Test
  void givenProduct_whenPostProduct_shouldCreateProduct() {
    // given
    String barcode = "00000000000009";
    String body = ApiData.loadApiData(ApiData.POST_PRODUCT);

    // when

    // then
  }

  @Test
  void givenProduct_whenPostProductAndBarcodeExists_shouldReturnBadRequest() {
    // given
    String body = ApiData.loadApiData(ApiData.POST_PRODUCT_EXISTS);

    // when

    // then
  }

  @Test
  void givenProduct_whenPutProduct_shouldUpdateProduct() {
    // given
    String barcode = "00000000000001";
    String body = ApiData.loadApiData(ApiData.PUT_PRODUCT);

    // when

    // then
  }

  @Test
  void givenProduct_whenDelete_shouldDeleteProduct() {
    // given
    String barcode = "00000000000001";

    // when

    // then
  }
}
