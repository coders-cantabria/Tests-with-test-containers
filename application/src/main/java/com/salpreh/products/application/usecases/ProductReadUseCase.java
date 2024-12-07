package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.application.ports.driving.ProductReadUseCasePort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductReadUseCase implements ProductReadUseCasePort {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public Optional<Product> getProduct(String barcode) {
    return productRepositoryPort.findByBarcode(barcode);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Product> getAll(int page, int size, ProductFilter filter) {
    return productRepositoryPort.findAll(page, size, filter);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exists(String barcode) {
    return productRepositoryPort.existsByBarcode(barcode);
  }
}
