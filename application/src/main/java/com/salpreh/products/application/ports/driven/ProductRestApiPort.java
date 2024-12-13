package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commons.IdName;
import reactor.core.publisher.Mono;
import java.util.List;

public interface ProductRestApiPort {

  Mono<List<Product>> getProducts();

  Mono<Product> createProduct(Product product);

  Mono<Product> getProduct(IdName<String> id);

  Mono<Product> updateProduct(IdName<String> id, Product product);

  void deleteProduct(IdName<String> id);
}
