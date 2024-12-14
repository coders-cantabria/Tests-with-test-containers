package com.salpreh.products.restapi.adapters;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRestApiAdapter implements ProductRepositoryPort {

  private final WebClient webClient;

  @Override
  public Optional<Product> findByBarcode(String barcode) {

    return Optional.ofNullable(
      webClient.get()
        .uri("/products/{id}", barcode)
        .retrieve()
        .bodyToMono(Product.class)
        .block()
    );
  }

  @Override
  public Page<Product> findAll(int page, int size, ProductFilter filter) {

    Mono<List<Product>> products = webClient.get()
      .uri("products")
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<Product>>() {});

    List<Product> productList = products.block();

    return new PageImpl<>(
      (productList != null) ? productList : List.of(),
      PageRequest.of(0, 5),
      2
    );
  }

  @Override
  public Product create(UpsertProductCommand createCommand) {

    return webClient.post()
      .uri("products")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(createCommand)
      .retrieve()
      .bodyToMono(Product.class)
      .block();
  }

  @Override
  public Product update(String barcode, UpsertProductCommand updateCommand) {
    return webClient.put()
      .uri("/products/{id}", barcode)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(updateCommand)
      .retrieve()
      .bodyToMono(Product.class)
      .block();
  }

  @Override
  public boolean existsByBarcode(String barcode) {

    return Boolean.TRUE.equals(webClient.get()
      .uri("products/exists/{id}", barcode)
      .retrieve()
      .bodyToMono(Boolean.class)
      .block());
  }

  @Override
  public void delete(String barcode) {

    webClient.delete()
      .uri("/products/{id}", barcode)
      .retrieve();
  }
}
