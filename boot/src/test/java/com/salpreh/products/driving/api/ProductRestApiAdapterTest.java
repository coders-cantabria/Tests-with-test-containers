package com.salpreh.products.driving.api;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockserver.model.JsonBody.json;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.driving.api.models.ApiPage;
import java.util.List;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestApiAdapterTest {

  private static final String MOCKSERVER_IMAGE = "mockserver/mockserver";
  private static final Integer MOCKSERVER_PORT = 1080;
  private static final String PRODUCTS_PATH = "/products";

  static MockServerClient mockServerClient;

  static void mockServerProperties(DynamicPropertyRegistry registry) {

    // TODO

    // Set to the application properties the obtained values from container
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

    // TODO
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

    // TODO
  }

  @Test
  void getProduct() {

    mockServerResponseGetProduct();

    // TODO
  }

  @Test
  void updateProduct() {

    mockServerResponsePutProduct();

    // TODO
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

    // TODO
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
