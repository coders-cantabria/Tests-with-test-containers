package com.salpreh.products.driven.database.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.models.commons.Range;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.driven.database.models.ProductEntity;
import com.salpreh.products.driven.database.repositories.filtering.ProductFiltering;
import com.salpreh.products.tests.utils.Scripts;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
public class ProductRepositoryITTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    loadData(Scripts.DELETE_ALL);
    loadData(Scripts.DATA_ALL);
  }

  @Test
  void givenProducts_whenFindWithFilter_thenReturnProducts() {
    // given
    ProductFilter filter = ProductFilter.builder()
      .tags(Set.of("lacteos", "sin gluten"))
      .purchasePriceRange(Range.of(1.0, 2.0))
      .supplierIds(Set.of(3L))
      .build();
    Specification<ProductEntity> spec = ProductFiltering.process(filter);

    // when
    Page<ProductEntity> productsPage = productRepository.findAll(spec, PageRequest.of(0, 100));

    // then
    assertEquals(1, productsPage.getTotalPages());
    assertEquals(3, productsPage.getTotalElements());
    assertEquals(3, productsPage.getContent().size());

    ProductEntity product = productsPage.getContent().get(0);
    assertEquals("00000000000003", product.getBarcode());
    assertEquals("Leche de vaca", product.getName());
  }


  @Test
  void givenProducts_whenGetAllWithNameFilter_shouldReturnFiltered() {
    // given
    ProductFilter filter = ProductFilter.builder()
      .search("Dietetico")
      .build();
    Specification<ProductEntity> spec = ProductFiltering.process(filter);

    // when
    Page<ProductEntity> productsPage = productRepository.findAll(spec, PageRequest.of(0, 100));

    // then
    assertEquals(1, productsPage.getTotalPages());
    assertEquals(1, productsPage.getTotalElements());
    assertEquals(1, productsPage.getContent().size());

    ProductEntity product = productsPage.getContent().get(0);
    assertEquals("00000000000004", product.getBarcode());
    assertTrue(product.getName().contains("dietético"));
  }

  protected void loadData(String sqlScript) {
    jdbcTemplate.execute(Scripts.loadScript(sqlScript));
  }
}
