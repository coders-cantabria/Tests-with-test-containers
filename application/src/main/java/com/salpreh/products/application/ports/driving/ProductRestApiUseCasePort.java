package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.Product;

import java.util.List;

public interface ProductRestApiUseCasePort {

    List<Product> getProducts();

    Product createProduct(Product post);

    Product getPost(String id);

    Product updatePost(String id, Product post);

    void deletePost(String id);
}
