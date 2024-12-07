package com.salpreh.products.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.exceptions.ProductNotFoundException;
import com.salpreh.products.application.exceptions.SupplierNotFoundException;
import com.salpreh.products.application.exceptions.base.ValidationException;
import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.Supplier;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.application.ports.driving.ProductWriteUseCasePort;
import com.salpreh.products.tests.base.DbSpringbootITTest;
import com.salpreh.products.tests.utils.Scripts;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductWriteUseCaseITTest extends DbSpringbootITTest {

  @Autowired
  private ProductWriteUseCasePort productWriteUseCase;

  @Autowired
  private ProductRepositoryPort productRepository;

  @BeforeEach
  void beforeEach() {
    loadData(Scripts.DELETE_ALL);
    loadData(Scripts.DATA_ALL);
  }

  @Test
  void givenProduct_whenCreate_shouldCreateProduct() {
    // given
    String barcode = "00000000000009";
    UpsertProductCommand createCommand = createUpsertCommand(
      barcode,
      "Product 1",
      10.0,
      10.0,
      List.of(1L, 2L),
      List.of("tag1")
    );

    // when
    Product product = productWriteUseCase.create(createCommand);

    // then
    Product dbProduct = productRepository.findByBarcode(barcode).orElseThrow();
    assertEquals(product, dbProduct);

    assertEquals(barcode, product.barcode());
    assertEquals("Product 1", product.name());
    assertEquals(2, product.suppliers().size());
    assertEquals(1, product.tags().size());

    assertEquals(1L, product.suppliers().get(0).id());
    assertNotNull(product.suppliers().get(0).name());
    assertEquals(2L, product.suppliers().get(1).id());
    assertNotNull(product.suppliers().get(1).name());
  }

  @Test
  void givenProduct_whenCreateAndSupplierDoNotExists_shouldThrowException() {
    // given
    String barcode = "00000000000009";
    UpsertProductCommand createCommand = createUpsertCommand(
      barcode,
      "Product 1",
      10.0,
      10.0,
      List.of(1L, 9L),
      List.of("tag1")
    );

    // when
    Executable create = () -> productWriteUseCase.create(createCommand);

    // then
    assertThrows(SupplierNotFoundException.class, create);
  }

  @Test
  void givenProduct_whenCreateAndBarcodeExists_shouldThrowException() {
    // given
    String barcode = "00000000000001";
    UpsertProductCommand createCommand = createUpsertCommand(
      barcode,
      "Product 1",
      10.0,
      10.0,
      List.of(1L, 2L),
      List.of("tag1")
    );

    // when
    Executable create = () -> productWriteUseCase.create(createCommand);

    // then
    assertThrows(ValidationException.class, create);
  }

  @Test
  void givenProduct_whenUpdateProduct_shouldUpdateProduct() {
    // given
    String barcode = "00000000000001";
    UpsertProductCommand updateCommand = createUpsertCommand(
      barcode,
      "Updated product",
      10.0,
      10.0,
      List.of(3L),
      List.of("tag1", "tag2")
    );

    // when
    Product product = productWriteUseCase.update(barcode, updateCommand);

    // then
    Product dbProduct = productRepository.findByBarcode(barcode).orElseThrow();
    assertEquals(product, dbProduct);

    assertEquals(barcode, product.barcode());
    assertEquals("Updated product", product.name());
    assertEquals(10.0, product.purchasePrice());
    assertEquals(10.0, product.sellingPrice());
    assertEquals(1, product.suppliers().size());
    assertEquals(2, product.tags().size());

    Supplier supplier = product.suppliers().get(0);
    assertEquals(3L, supplier.id());
    assertNotNull(supplier.name());
  }

  @Test
  void givenProduct_whenUpdateProductAndSupplierDoNotExists_shouldThrowException() {
    // given
    String barcode = "00000000000001";
    UpsertProductCommand updateCommand = createUpsertCommand(
      barcode,
      "Updated product",
      10.0,
      10.0,
      List.of(1L, 9L),
      List.of("tag1", "tag2")
    );

    // when
    Executable update = () -> productWriteUseCase.update(barcode, updateCommand);

    // then
    assertThrows(SupplierNotFoundException.class, update);
  }

  @Test
  void givenProduct_whenUpdateProductAndProductDoNotExists_shouldThrowException() {
    // given
    String barcode = "00000000000009";
    UpsertProductCommand updateCommand = createUpsertCommand(
      barcode,
      "Updated product",
      10.0,
      10.0,
      List.of(1L, 2L),
      List.of("tag1", "tag2")
    );

    // when
    Executable update = () -> productWriteUseCase.update(barcode, updateCommand);

    // then
    assertThrows(ProductNotFoundException.class, update);
  }

  @Test
  void givenProduct_whenDeleteProduct_shouldDeleteProduct() {
    // given
    String barcode = "00000000000001";
    boolean existsPrevious = productRepository.existsByBarcode(barcode);

    // when
    productWriteUseCase.delete(barcode);

    // then
    assertTrue(existsPrevious);
    assertFalse(productRepository.existsByBarcode(barcode));
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
