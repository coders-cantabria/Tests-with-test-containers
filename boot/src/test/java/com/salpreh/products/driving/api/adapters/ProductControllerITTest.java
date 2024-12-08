package com.salpreh.products.driving.api.adapters;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.tests.base.DbSpringbootITTest;
import com.salpreh.products.tests.utils.ApiData;
import com.salpreh.products.tests.utils.Scripts;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerITTest extends DbSpringbootITTest {

  @LocalServerPort
  private Integer port;

  @Autowired
  private ProductRepositoryPort productRepository;

  @BeforeEach
  void setUp() {
    loadData(Scripts.DELETE_ALL);
    loadData(Scripts.DATA_ALL);

    baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  void givenProducts_whenGetAllProducts_shouldReturnAll() {
    // given
    int page = 0;
    int size = 100;

    // when
    Response response = given()
      .when()
      .queryParams(
        "page", page,
        "size", size
      )
      .get("/products");

    // then
    response.then()
      .log().all()
      .statusCode(200)
      .assertThat()
      // data
      .body("data", hasSize(5))
      .body("data[0].barcode", equalTo("00000000000001"))
      .body("data[0].name", equalTo("Arroz de Valencia"))
      .body("data[0].suppliers", hasSize(2))
      .body("data[0].tags", hasSize(1))
      // page
      .body("page.requestedPage", equalTo(page))
      .body("page.totalPages", equalTo(1))
      .body("page.totalElements", equalTo(5));
  }

  @Test
  void givenProducts_whenGetAllWithFilters_shouldReturnFiltered() {
    // given
    int page = 0;
    int size = 100;
    String tags = "lacteos,sin gluten";
    String purchasePriceRange = "1.0,2.0";
    String supplierIds = "3";

    // when
    Response response = given()
      .when()
      .queryParams(
        "page", page,
        "size", size,
        "tags", tags,
        "purchasePriceRange", purchasePriceRange,
        "supplierIds", supplierIds
      )
      .get("/products");

    // then
    response.then()
      .log().all()
      .statusCode(200)
      .assertThat()
      // data
      .body("data", hasSize(3))
      .body("data[0].barcode", equalTo("00000000000003"))
      .body("data[0].name", equalTo("Leche de vaca"))
      .body("data[0].suppliers", hasSize(1))
      .body("data[0].tags", hasSize(1))
      // page
      .body("page.requestedPage", equalTo(page))
      .body("page.totalPages", equalTo(1))
      .body("page.totalElements", equalTo(3));
  }

  @Test
  void givenProduct_whenGetByBarcode_shouldReturnProduct() {
    // given
    String barcode = "00000000000001";

    // when
    Response response = given()
      .when()
      .get("/products/{barcode}", barcode);

    // then
    response
      .then()
      .log().all()
      .statusCode(200)
      .assertThat()
      .body("barcode", equalTo(barcode))
      .body("name", equalTo("Arroz de Valencia"))
      .body("suppliers", hasSize(2))
      .body("tags", hasSize(1));
  }

  @Test
  void givenProduct_whenGetByBarcodeAndDoNotExists_shouldReturnNotFound() {
    // given
    String barcode = "00000000000099";

    // when
    Response response = given()
      .when()
      .get("/products/{barcode}", barcode);

    // then
    response
      .then()
      .log().all()
      .statusCode(404);
  }

  @Test
  void givenProduct_whenPostProduct_shouldCreateProduct() {
    // given
    String barcode = "00000000000009";
    String body = ApiData.loadApiData(ApiData.POST_PRODUCT);

    // when
    Response response = given()
      .when()
      .body(body)
      .contentType(ContentType.JSON)
      .post("/products");

    // then
    response
      .then()
      .log().all()
      .statusCode(201)
      .assertThat()
      .body("barcode", equalTo(barcode))
      .body("name", equalTo("Arroz con leche"))
      .body("suppliers", hasSize(2))
      .body("tags", hasSize(2));

    assertTrue(productRepository.existsByBarcode(barcode));
  }

  @Test
  void givenProduct_whenPostProductAndBarcodeExists_shouldReturnBadRequest() {
    // given
    String body = ApiData.loadApiData(ApiData.POST_PRODUCT_EXISTS);

    // when
    Response response = given()
      .when()
      .body(body)
      .contentType(ContentType.JSON)
      .post("/products");

    // then
    response
      .then()
      .log().all()
      .statusCode(400);
  }

  @Test
  void givenProduct_whenPutProduct_shouldUpdateProduct() {
    // given
    String barcode = "00000000000001";
    String body = ApiData.loadApiData(ApiData.PUT_PRODUCT);

    // when
    Response response = given()
      .when()
      .body(body)
      .contentType(ContentType.JSON)
      .put("/products/{barcode}", barcode);

    // then
    response
      .then()
      .log().all()
      .statusCode(200)
      .assertThat()
      .body("barcode", equalTo(barcode))
      .body("name", equalTo("Arroz con leche"))
      .body("suppliers", hasSize(1))
      .body("tags", hasSize(1));
  }

  @Test
  void givenProduct_whenDelete_shouldDeleteProduct() {
    // given
    String barcode = "00000000000001";
    boolean previouslyExists = productRepository.existsByBarcode(barcode);

    // when
    Response response = given()
      .when()
      .delete("/products/{barcode}", barcode);

    // then
    response
      .then()
      .log().all()
      .statusCode(204);

    assertTrue(previouslyExists);
    assertFalse(productRepository.existsByBarcode(barcode));
  }
}
