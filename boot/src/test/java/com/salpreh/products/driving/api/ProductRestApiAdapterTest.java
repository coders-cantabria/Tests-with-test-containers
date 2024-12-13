package com.salpreh.products.driving.api;

import com.salpreh.products.application.models.Product;
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
class ProductRestApiAdapterTest {

  private static final Integer MOCKSERVER_PORT = 1080;
  private static final String MOCKSERVER_SERVICE_NAME = "mockServer_1";
  private static final String POSTS_PATH = "/posts";

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
  void getPosts() {

    ResponseEntity<List<Product>> response = testRestTemplate.exchange(
      POSTS_PATH,
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<>() {}
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    assertPostsPropertiesFromGetPosts(response.getBody());
  }

  private void assertPostsPropertiesFromGetPosts(List<Product> results) {

    assertAll(
      () -> Assertions.assertNotNull(results),
      () -> Assertions.assertEquals(2, results.size()),
      () -> Assertions.assertEquals("Barcode name", results.get(0).barcode()),
      () -> Assertions.assertEquals("Product name", results.get(0).name()),
      () -> Assertions.assertEquals("Description", results.get(0).description()),
      () -> Assertions.assertEquals("Image url", results.get(0).imageUrl()),
      () -> Assertions.assertEquals(7.5, results.get(1).purchasePrice()),
      () -> Assertions.assertEquals(8.5, results.get(1).sellingPrice())
    );
  }

  @Test
  void createPost() {

    Product product = new Product(
      "Barcode name",
      "Product name",
      "Description",
      "Image url",
      7.5,
      8.5,
      List.of(),
      List.of()
    );

    Product response = testRestTemplate.postForObject(POSTS_PATH, product, Product.class);

    Assertions.assertNotNull(response);

    Assertions.assertNotNull(response);
    Assertions.assertEquals("Barcode name", response.barcode());
    Assertions.assertEquals("Product name", response.name());
    Assertions.assertEquals("Description", response.description());
    Assertions.assertEquals("Image url", response.imageUrl());
    Assertions.assertEquals(7.5, response.purchasePrice());
    Assertions.assertEquals(8.5, response.sellingPrice());
    Assertions.assertEquals(List.of(), response.suppliers());
    Assertions.assertEquals(List.of(), response.tags());
  }

  @Test
  void getPost() {

    Product response =
      testRestTemplate.getForObject(String.format("%s/%d", POSTS_PATH, 1), Product.class);

    Assertions.assertNotNull(response);

    Assertions.assertEquals("Barcode name", response.barcode());
    Assertions.assertEquals("Product name", response.name());
    Assertions.assertEquals("Description", response.description());
    Assertions.assertEquals("Image url", response.imageUrl());
    Assertions.assertEquals(7.5, response.purchasePrice());
    Assertions.assertEquals(8.5, response.sellingPrice());
    Assertions.assertEquals(List.of(), response.suppliers());
    Assertions.assertEquals(List.of(), response.tags());
  }

  @Test
  void updatePost() {

    Product product = new Product(
      "Barcode name updated",
      "Product name updated",
      "Description updated",
      "Image url updated",
      7.6,
      8.6,
      List.of(),
      List.of()
    );

    HttpEntity<Product> requestEntity = new HttpEntity<>(product);

    ResponseEntity<Product> response = testRestTemplate.exchange(
      String.format("%s/%d", POSTS_PATH, 1),
      HttpMethod.PUT,
      requestEntity,
      Product.class
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    assertPostsPropertiesFromPutPost(response.getBody());
  }

  private void assertPostsPropertiesFromPutPost(Product product) {

    assertAll(
      () -> Assertions.assertEquals("Barcode name", product.barcode()),
      () -> Assertions.assertEquals("Product name", product.name()),
      () -> Assertions.assertEquals("Description", product.description()),
      () -> Assertions.assertEquals("Image url", product.imageUrl()),
      () -> Assertions.assertEquals(7.6, product.purchasePrice()),
      () -> Assertions.assertEquals(8.6, product.sellingPrice())
    );
  }

  @Test
  void deletePost() {

    ResponseEntity<Void> response = testRestTemplate.exchange(
      String.format("%s/%d", POSTS_PATH, 1),
      HttpMethod.DELETE,
      null,
      Void.class
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
