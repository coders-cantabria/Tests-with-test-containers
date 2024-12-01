package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StoreRepositoryPort {
  Optional<Store> findByCode(long code);
  Page<Store> findAll(int page, int size);
  boolean existsByCode(long code);
}
