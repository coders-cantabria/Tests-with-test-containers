package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.commons.Range;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.tests.base.DbSpringbootITTest;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductReadUseCaseITTest extends DbSpringbootITTest {

  @BeforeEach
  void setUp() {
    // TODO: Prepare tests setup
  }

  @Test
  void givenProducts_whenGetAllWithoutFilter_shouldReturnAll() {
    // given
    ProductFilter filter = ProductFilter.empty();

    // when

    // then
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

    // then
  }

  @Test
  void givenProducts_whenGetAllWithNameFilter_shouldReturnFiltered() {
    // given
    ProductFilter filter = ProductFilter.builder()
      .search("Dietetico")
      .build();

    // when

    // then
  }

  @Test
  void givenProducts_whenGetOneAndExists_shouldReturn() {
    // given

    // when

    // then
  }

  @Test
  void givenProducts_whenGetOneAndNotExists_shouldReturnEmpty() {
    // given

    // when

    // then
  }

  @Test
  void givenProducts_whenExistsCheck_shouldReturnTrue() {
    // given

    // when

    // then
  }

  @Test
  void givenProducts_whenExistsCheckAndNotExists_shouldReturnFalse() {
    // given

    // when

    // then
  }

}
