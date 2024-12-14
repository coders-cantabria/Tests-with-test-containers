package com.salpreh.products.driving.api;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.driving.api.models.ApiPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestApiAdapterComposeTest {

  private static final Integer MOCKSERVER_PORT = 1080;
  private static final String MOCKSERVER_SERVICE_NAME = "mockServer_1";
  private static final String PRODUCTS_PATH = "/products";

  @Container
  static DockerComposeContainer<?> dockerComposeContainer = new DockerComposeContainer(
    new File("src/test/resources/compose/compose.yml"))
    .withExposedService(MOCKSERVER_SERVICE_NAME, MOCKSERVER_PORT);

  @DynamicPropertySource
  static void mockServerProperties(DynamicPropertyRegistry registry) {

    dockerComposeContainer.start();

    // Set to the application properties the obtained values
    // In this case, the service name must be specified because in docker compose we can
    // use as many services as we need
    registry.add(
      "mockserver.host",
      () -> dockerComposeContainer.getServiceHost(MOCKSERVER_SERVICE_NAME, MOCKSERVER_PORT)
    );
    registry.add(
      "mockserver.port",
      () -> dockerComposeContainer.getServicePort(MOCKSERVER_SERVICE_NAME, MOCKSERVER_PORT)
    );
  }

  @Autowired
  TestRestTemplate testRestTemplate;

  @Test
  void getProducts() {

    ResponseEntity<ApiPage<Product>> response = testRestTemplate.exchange(
      PRODUCTS_PATH,
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<>() {}
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    assertProductsPropertiesFromGetProducts(response.getBody().getData());
  }

  private void assertProductsPropertiesFromGetProducts(List<Product> results) {

    assertAll(
      () -> Assertions.assertNotNull(results),
      () -> Assertions.assertEquals(2, results.size()),
      () -> Assertions.assertEquals("Barcode name", results.get(0).barcode()),
      () -> Assertions.assertEquals("Product name", results.get(0).name()),
      () -> Assertions.assertEquals("Description", results.get(0).description()),
      () -> Assertions.assertEquals("Image url", results.get(0).imageUrl()),
      () -> Assertions.assertEquals(7.5, results.get(0).purchasePrice()),
      () -> Assertions.assertEquals(8.5, results.get(0).sellingPrice())
    );
  }

  @Test
  void createProduct() {

        UpsertProductCommand upsertProductCommand = new UpsertProductCommand(
      "Barcode name create",
      "Product name create",
      "Description create",
      "Image url create",
      1.2,
      1.8,
      List.of(),
      List.of()
    );

    Product response = testRestTemplate.postForObject(PRODUCTS_PATH, upsertProductCommand, Product.class);

    Assertions.assertNotNull(response);

    Assertions.assertNotNull(response);
    Assertions.assertEquals("Barcode name create", response.barcode());
    Assertions.assertEquals("Product name create", response.name());
    Assertions.assertEquals("Description create", response.description());
    Assertions.assertEquals("Image url create", response.imageUrl());
    Assertions.assertEquals(1.2, response.purchasePrice());
    Assertions.assertEquals(1.8, response.sellingPrice());
    Assertions.assertNull(response.suppliers());
    Assertions.assertNull(response.tags());
  }

  @Test
  void getProduct() {

    Product response =
      testRestTemplate.getForObject(String.format("%s/%d", PRODUCTS_PATH, 1), Product.class);

    Assertions.assertNotNull(response);

    Assertions.assertEquals("Barcode name", response.barcode());
    Assertions.assertEquals("Product name", response.name());
    Assertions.assertEquals("Description", response.description());
    Assertions.assertEquals("Image url", response.imageUrl());
    Assertions.assertEquals(7.5, response.purchasePrice());
    Assertions.assertEquals(8.5, response.sellingPrice());
    Assertions.assertNull(response.suppliers());
    Assertions.assertNull(response.tags());
  }

  @Test
  void updateProduct() {

    UpsertProductCommand upsertProductCommand = new UpsertProductCommand(
      "Barcode name updated",
      "Product name updated",
      "Description updated",
      "Image url updated",
      7.6,
      8.6,
      List.of(),
      List.of()
    );

    HttpEntity<UpsertProductCommand> requestEntity = new HttpEntity<>(upsertProductCommand);

    ResponseEntity<Product> response = testRestTemplate.exchange(
      String.format("%s/%d", PRODUCTS_PATH, 1),
      HttpMethod.PUT,
      requestEntity,
      Product.class
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    assertProductsPropertiesFromPutProduct(response.getBody());
  }

  private void assertProductsPropertiesFromPutProduct(Product product) {

    assertAll(
      () -> Assertions.assertEquals("Barcode name updated", product.barcode()),
      () -> Assertions.assertEquals("Product name updated", product.name()),
      () -> Assertions.assertEquals("Description updated", product.description()),
      () -> Assertions.assertEquals("Image url updated", product.imageUrl()),
      () -> Assertions.assertEquals(7.6, product.purchasePrice()),
      () -> Assertions.assertEquals(8.6, product.sellingPrice())
    );
  }

  @Test
  void deleteProduct() {

    ResponseEntity<Void> response = testRestTemplate.exchange(
      String.format("%s/%d", PRODUCTS_PATH, 1),
      HttpMethod.DELETE,
      null,
      Void.class
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
