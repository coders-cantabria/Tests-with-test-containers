package com.salpreh.products.driving.api;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.driving.api.models.ApiPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockserver.model.JsonBody.json;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestApiAdapterTest {

  private static final String MOCKSERVER_IMAGE = "mockserver/mockserver";
  private static final Integer MOCKSERVER_PORT = 1080;
  private static final String PRODUCTS_PATH = "/products";

  static GenericContainer<?> mockServer = new GenericContainer<>(
    DockerImageName.parse(MOCKSERVER_IMAGE))
    .withExposedPorts(MOCKSERVER_PORT);

  static MockServerClient mockServerClient;

  @DynamicPropertySource
  static void mockServerProperties(DynamicPropertyRegistry registry) {

    mockServer.start();

    // Set to the application properties the obtained values
    registry.add("mockserver.host", mockServer::getHost);
    registry.add("mockserver.port", mockServer::getFirstMappedPort);

    mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getFirstMappedPort());
  }

  @Autowired
  TestRestTemplate testRestTemplate;

  @Test
  void getProducts() {

    mockServerResponseGetProducts();

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

    mockServerResponsePostProduct();

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

    mockServerResponseGetProduct();

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

    mockServerResponsePutProduct();

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

    mockServerResponseDeleteProduct();

    ResponseEntity<Void> response = testRestTemplate.exchange(
      String.format("%s/%d", PRODUCTS_PATH, 1),
      HttpMethod.DELETE,
      null,
      Void.class
    );

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  private void mockServerResponseGetProducts() {

    mockServerClient.when(
      HttpRequest.request()
        .withMethod("GET")
        .withPath(PRODUCTS_PATH)
    ).respond(
      HttpResponse.response()
        .withStatusCode(200)
        .withHeaders(new Header("Content-Type", "application/json"))
        .withBody(json("""
          [
            {
              "barcode": "Barcode name",
              "name": "Product name",
              "description": "Description",
              "imageUrl": "Image url",
              "purchasePrice": 7.5,
              "sellingPrice": 8.5
            },
            {
              "barcode": "Barcode name 2",
              "name": "Product name 2",
              "description": "Description 2",
              "imageUrl": "Image url 2",
             "purchasePrice": 8.5,
             "sellingPrice": 9.5
            }
          ]
          """))
    );
  }

  private void mockServerResponsePostProduct() {

    mockServerClient.when(
      HttpRequest.request()
        .withMethod("POST")
        .withPath(PRODUCTS_PATH)
    ).respond(
      HttpResponse.response()
        .withStatusCode(200)
        .withHeaders(new Header("Content-Type", "application/json"))
        .withBody(json("""
          {
              "barcode": "Barcode name create",
              "name": "Product name create",
              "description": "Description create",
              "imageUrl": "Image url create",
              "purchasePrice": 1.2,
              "sellingPrice": 1.8
          }
          """))
    );
  }

  private void mockServerResponseGetProduct() {

    mockServerClient.when(
      HttpRequest.request()
        .withMethod("GET")
        .withPath(String.format("%s/%d", PRODUCTS_PATH, 1))
    ).respond(
      HttpResponse.response()
        .withStatusCode(201)
        .withHeaders(new Header("Content-Type", "application/json"))
        .withBody(json("""
          {
             "barcode": "Barcode name",
             "name": "Product name",
             "description": "Description",
             "imageUrl": "Image url",
             "purchasePrice": 7.5,
             "sellingPrice": 8.5
          }
          """))
    );
  }

  private void mockServerResponsePutProduct() {

    mockServerClient.when(
      HttpRequest.request()
        .withMethod("PUT")
        .withPath(String.format("%s/%d", PRODUCTS_PATH, 1))
    ).respond(
      HttpResponse.response()
        .withStatusCode(200)
        .withHeaders(new Header("Content-Type", "application/json"))
        .withBody(json("""
          {
            "barcode": "Barcode name updated",
            "name": "Product name updated",
            "description": "Description updated",
            "imageUrl": "Image url updated",
            "purchasePrice": 7.6,
            "sellingPrice": 8.6
          }
          """))
    );
  }

  private void mockServerResponseDeleteProduct() {

    mockServerClient.when(
      HttpRequest.request()
        .withMethod("DELETE")
        .withPath(String.format("%s/%d", PRODUCTS_PATH, 1))
    ).respond(
      HttpResponse.response()
        .withStatusCode(204)
    );
  }
}
