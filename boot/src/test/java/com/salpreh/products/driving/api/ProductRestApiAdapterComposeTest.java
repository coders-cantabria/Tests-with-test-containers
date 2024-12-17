package com.salpreh.products.driving.api;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.driving.api.models.ApiPage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestApiAdapterComposeTest {

  private static final Integer MOCKSERVER_PORT = 1080;
  private static final String MOCKSERVER_SERVICE_NAME = "mockServer_1";
  private static final String PRODUCTS_PATH = "/products";


  static void mockServerProperties(DynamicPropertyRegistry registry) {

    // TODO

    // Set to the application properties the obtained values
    // In this case, the service name must be specified because in docker compose we can
    // use as many services as we need

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

    // TODO
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

    Product response =
      testRestTemplate.postForObject(PRODUCTS_PATH, upsertProductCommand, Product.class);

    // TODO
  }

  @Test
  void getProduct() {

    // TODO
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

    // TODO
  }

  @Test
  void deleteProduct() {

    // TODO
  }
}
