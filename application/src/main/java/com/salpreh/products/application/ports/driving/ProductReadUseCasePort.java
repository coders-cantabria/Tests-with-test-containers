package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.filters.ProductFilter;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductReadUseCasePort {
  Optional<Product> getProduct(String barcode);
  Page<Product> getAll(int page, int size, ProductFilter filter);
  boolean exists(String barcode);
}
