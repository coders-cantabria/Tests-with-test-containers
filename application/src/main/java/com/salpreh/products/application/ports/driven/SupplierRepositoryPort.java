package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.Supplier;
import java.util.Optional;

public interface SupplierRepositoryPort {
  Optional<Supplier> findById(Long id);
  boolean existsById(Long id);
}
