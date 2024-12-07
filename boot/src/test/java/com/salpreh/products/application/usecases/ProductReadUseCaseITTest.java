package com.salpreh.products.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commons.Range;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.application.ports.driving.ProductReadUseCasePort;
import com.salpreh.products.tests.base.DbSpringbootITTest;
import com.salpreh.products.tests.utils.Scripts;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class ProductReadUseCaseITTest extends DbSpringbootITTest {

  @Autowired
  public ProductReadUseCasePort productReadUseCasePort;

  @BeforeEach
  void beforeEach() {
    loadData(Scripts.DELETE_ALL);
    loadData(Scripts.DATA_ALL);
  }

  @Test
  void givenProducts_whenGetAllWithoutFilter_shouldReturnAll() {
    // given
    ProductFilter filter = ProductFilter.empty();

    // when
    Page<Product> productsPage = productReadUseCasePort.getAll(0, 100, filter);

    // then
    assertEquals(1, productsPage.getTotalPages());
    assertEquals(5, productsPage.getTotalElements());
    assertEquals(5, productsPage.getContent().size());

    Product product = productsPage.getContent().get(0);
    assertEquals("00000000000001", product.barcode());
    assertEquals("Arroz de Valencia", product.name());
    assertEquals(5.0, product.purchasePrice());
  }

  @Test
  void givenProducts_whenGetAllWithFilters_shouldReturnFiltered() {
    // given
    ProductFilter filter = ProductFilter.builder()
      .tags(Set.of("lacteos", "sin gluten"))
      .purchasePriceRange(Range.of(1.0, 2.0))
      .supplierIds(Set.of(3L))
      .build();

    // when
    Page<Product> productsPage = productReadUseCasePort.getAll(0, 100, filter);

    // then
    assertEquals(1, productsPage.getTotalPages());
    assertEquals(3, productsPage.getTotalElements());
    assertEquals(3, productsPage.getContent().size());

    Product product = productsPage.getContent().get(0);
    assertEquals("00000000000003", product.barcode());
    assertEquals("Leche de vaca", product.name());
  }

  @Test
  void givenProducts_whenGetAllWithNameFilter_shouldReturnFiltered() {
    // given
    ProductFilter filter = ProductFilter.builder()
      .search("Dietetico")
      .build();

    // when
    Page<Product> productsPage = productReadUseCasePort.getAll(0, 100, filter);

    // then
    assertEquals(1, productsPage.getTotalPages());
    assertEquals(1, productsPage.getTotalElements());
    assertEquals(1, productsPage.getContent().size());

    Product product = productsPage.getContent().get(0);
    assertEquals("00000000000004", product.barcode());
    assertTrue(product.name().contains("dietético"));
  }

  @Test
  void givenProducts_whenGetOneAndExists_shouldReturn() {
    // when
    Product product = productReadUseCasePort.getProduct("00000000000001")
      .orElseThrow();

    // then
    assertEquals("00000000000001", product.barcode());
    assertEquals("Arroz de Valencia", product.name());
    assertEquals(5.0, product.purchasePrice());
  }

  @Test
  void givenProducts_whenGetOneAndNotExists_shouldReturnEmpty() {
    // when
    var product = productReadUseCasePort.getProduct("00000000000006");

    // then
    assertTrue(product.isEmpty());
  }

  @Test
  void givenProducts_whenExistsCheck_shouldReturnTrue() {
    // when
    boolean exists = productReadUseCasePort.exists("00000000000001");

    // then
    assertTrue(exists);
  }

  @Test
  void givenProducts_whenExistsCheckAndNotExists_shouldReturnFalse() {
    // when
    boolean exists = productReadUseCasePort.exists("00000000000006");

    // then
    assertFalse(exists);
  }

}
