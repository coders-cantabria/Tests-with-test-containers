package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.tests.base.DbSpringbootITTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductWriteUseCaseITTest extends DbSpringbootITTest {

  @BeforeEach
  void setUp() {
    // TODO: Prepare tests setup
  }

  @Test
  void givenProduct_whenCreate_shouldCreateProduct() {
    // given
    String barcode = "00000000000009";

    // when

    // then
  }

  @Test
  void givenProduct_whenCreateAndSupplierDoNotExists_shouldThrowException() {
    // given
    String barcode = "00000000000009";

    // when

    // then
  }

  @Test
  void givenProduct_whenCreateAndBarcodeExists_shouldThrowException() {
    // given
    String barcode = "00000000000001";

    // when

    // then
  }

  @Test
  void givenProduct_whenUpdateProduct_shouldUpdateProduct() {
    // given
    String barcode = "00000000000001";

    // when

    // then
  }

  @Test
  void givenProduct_whenUpdateProductAndSupplierDoNotExists_shouldThrowException() {
    // given
    String barcode = "00000000000001";

    // when

    // then
  }

  @Test
  void givenProduct_whenUpdateProductAndProductDoNotExists_shouldThrowException() {
    // given
    String barcode = "00000000000009";

    // when

    // then
  }

  @Test
  void givenProduct_whenDeleteProduct_shouldDeleteProduct() {
    // given
    String barcode = "00000000000001";

    // when

    // then
  }

  private UpsertProductCommand createUpsertCommand(String barcode, String name, Double purchasePrice, Double sellingPrice, List<Long> suppliers, List<String> tags) {
    return new UpsertProductCommand(
      barcode,
      name,
      "Description: " + name,
      "http://image.com",
      purchasePrice,
      sellingPrice,
      suppliers,
      tags
    );
  }
}
