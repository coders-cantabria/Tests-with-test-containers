package com.salpreh.products.driven.database.repositories;

import com.salpreh.products.application.models.commons.Range;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.tests.utils.Scripts;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.jdbc.core.JdbcTemplate;

// TODO: Missing data jpa test configuration
@AutoConfigureTestDatabase(replace = Replace.NONE) // Needed to avoid H2 bootstrapping when using spring data tests
public class ProductRepositoryITTest {

  // TODO: Create you testcontainers and configure them

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    // TODO: Load data and whatever you think is necessary
  }

  @Test
  void givenProducts_whenFindWithFilter_thenReturnProducts() {
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

  protected void loadData(String sqlScript) {
    jdbcTemplate.execute(Scripts.loadScript(sqlScript));
  }
}
