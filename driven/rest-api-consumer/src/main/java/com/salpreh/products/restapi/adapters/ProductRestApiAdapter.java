package com.salpreh.products.restapi.adapters;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commons.IdName;
import com.salpreh.products.application.ports.driven.ProductRestApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRestApiAdapter implements ProductRestApiPort {

    private final WebClient webClient;

    @Override
    public Mono<List<Product>> getProducts() {

      return webClient.get()
                .uri("products")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<Product> createProduct(Product product) {

        return webClient.post()
                    .uri("products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(product)
                    .retrieve()
                    .bodyToMono(Product.class);
    }

    @Override
    public Mono<Product> getProduct(IdName<String> id) {

        return webClient.get()
                .uri("/products/{id}", id.getId())
                .retrieve()
                .bodyToMono(Product.class);
    }

    @Override
    public Mono<Product> updateProduct(IdName<String> id, Product product) {

        return webClient.put()
                .uri("/products/{id}", id.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }

    @Override
    public void deleteProduct(IdName<String> id) {

        webClient.delete()
                .uri("/products/{id}", id.getId())
                .retrieve();
    }
}
