package com.salpreh.products.driven.database.adapters;

import com.salpreh.products.application.exceptions.ProductNotFoundException;
import com.salpreh.products.application.exceptions.SupplierNotFoundException;
import com.salpreh.products.application.exceptions.base.ValidationException;
import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.driven.database.mappers.ProductMapper;
import com.salpreh.products.driven.database.models.ProductEntity;
import com.salpreh.products.driven.database.models.SupplierEntity;
import com.salpreh.products.driven.database.repositories.ProductRepository;
import com.salpreh.products.driven.database.repositories.SupplierRepository;
import com.salpreh.products.driven.database.repositories.filtering.ProductFiltering;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

  private final ProductRepository productRepository;
  private final SupplierRepository supplierRepository;
  private final ProductMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Optional<Product> findByBarcode(String barcode) {
    return productRepository.findById(barcode)
      .map(mapper::toModel);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Product> findAll(int page, int size, ProductFilter filter) {
    Specification<ProductEntity> spec = ProductFiltering.process(filter);
    return productRepository.findAll(spec, PageRequest.of(page, size))
      .map(mapper::toModel);
  }

  @Override
  public Product create(UpsertProductCommand createCommand) {
    if (productRepository.existsById(createCommand.barcode()))
      throw new ValidationException("Product with barcode %s already exists".formatted(createCommand.barcode()));

    ProductEntity product = mapper.toEntity(createCommand);
    product.setSuppliers(getSuppliers(createCommand));

    product = productRepository.save(product);

    return mapper.toModel(product);
  }

  @Override
  public Product update(String barcode, UpsertProductCommand updateCommand) {
    ProductEntity product = productRepository.findById(barcode)
      .orElseThrow(() -> new ProductNotFoundException(barcode));
    mapper.toEntity(product, updateCommand);
    product.setSuppliers(getSuppliers(updateCommand));

    product = productRepository.save(product);

    return mapper.toModel(product);
  }

  @Override
  public boolean existsByBarcode(String barcode) {
    return productRepository.existsById(barcode);
  }

  @Override
  public void delete(String barcode) {
    productRepository.deleteById(barcode);
  }

  private Set<SupplierEntity> getSuppliers(UpsertProductCommand createCommand) throws SupplierNotFoundException {
    return createCommand.suppliers().stream()
      .map(id -> Pair.of(id, supplierRepository.findById(id)))
      .map(sd -> sd.getSecond().orElseThrow(() -> new SupplierNotFoundException(sd.getFirst())))
      .collect(Collectors.toSet());
  }
}
