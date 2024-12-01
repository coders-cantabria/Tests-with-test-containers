package com.salpreh.products.driven.database.adapters;

import com.salpreh.products.application.models.Supplier;
import com.salpreh.products.application.ports.driven.SupplierRepositoryPort;
import com.salpreh.products.driven.database.mappers.SupplierMapper;
import com.salpreh.products.driven.database.repositories.SupplierRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SupplierRepositoryAdapter implements SupplierRepositoryPort {

  private final SupplierRepository supplierRepository;
  private final SupplierMapper mapper;

  @Override
  public Optional<Supplier> findById(Long id) {
    return supplierRepository.findById(id)
      .map(mapper::toModel);
  }
}
