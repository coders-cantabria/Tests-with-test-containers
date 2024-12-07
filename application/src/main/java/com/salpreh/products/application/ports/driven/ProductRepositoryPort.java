package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.application.models.filters.ProductFilter;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductRepositoryPort {
  Optional<Product> findByBarcode(String barcode);
  Page<Product> findAll(int page, int size, ProductFilter filter);
  Product create(UpsertProductCommand createCommand);
  Product update(String barcode, UpsertProductCommand updateCommand);
  boolean existsByBarcode(String barcode);
  void delete(String barcode);
}
