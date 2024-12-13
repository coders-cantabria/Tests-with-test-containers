package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commons.IdName;
import com.salpreh.products.application.ports.driven.ProductRestApiPort;
import com.salpreh.products.application.ports.driving.ProductRestApiUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRestApiUseCase implements ProductRestApiUseCasePort {

    private final ProductRestApiPort productRestApiPort;

    @Override
    public List<Product> getPosts() {

        return productRestApiPort.getProducts().block();
    }

    @Override
    public Product createPost(Product post) {

        log.info("PostReceived to create: {}", post);

        Mono<Product> createdPost = productRestApiPort.createProduct(post);

        log.info("PostCreated {}", post);

        return createdPost.block();
    }

    @Override
    public Product getPost(String id) {

        return productRestApiPort.getProduct(IdName.of(id, null)).block();
    }

    @Override
    public Product updatePost(String id, Product post) {

        log.info("PostReceived to update: {}", post);

        Mono<Product> updatedPost = productRestApiPort.updateProduct(IdName.of(id, null), post);

        log.info("PostUpdated {}", post);

        return updatedPost.block();
    }

    @Override
    public void deletePost(String id) {

        log.info("Delete post with id {}", id);

        productRestApiPort.deleteProduct(IdName.of(id, null));

        log.info("PostDeleted with id: {}", id);
    }
}
