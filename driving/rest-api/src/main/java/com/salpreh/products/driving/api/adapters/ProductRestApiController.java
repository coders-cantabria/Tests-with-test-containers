package com.salpreh.products.driving.api.adapters;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.ports.driving.ProductRestApiUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/products-rest")
@RequiredArgsConstructor
public class ProductRestApiController {

  private final ProductRestApiUseCasePort productRestApiUseCasePort;

    @GetMapping
    public ResponseEntity<List<Product>> getPosts() {

        return ResponseEntity.ok(productRestApiUseCasePort.getProducts());
    }

    @PostMapping
    public ResponseEntity<Product> createPost(@RequestBody Product post) {

        return ResponseEntity.ok(productRestApiUseCasePort.createProduct(post));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> getPost(
            @PathVariable String id) {

        return ResponseEntity.ok(productRestApiUseCasePort.getPost(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> updatePost(
            @PathVariable String id,
            @RequestBody Product post) {

        return ResponseEntity.ok(productRestApiUseCasePort.updatePost(id, post));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String id) {

        productRestApiUseCasePort.deletePost(id);

        return ResponseEntity.noContent().build();
    }
}
