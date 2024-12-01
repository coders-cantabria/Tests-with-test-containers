package com.salpreh.products.driven.database.adapters;

import com.salpreh.products.application.models.Store;
import com.salpreh.products.application.ports.driven.StoreRepositoryPort;
import com.salpreh.products.driven.database.mappers.StoreMapper;
import com.salpreh.products.driven.database.repositories.StoreRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryAdapter implements StoreRepositoryPort {

  private final StoreRepository storeRepositoryPort;
  private final StoreMapper mapper;


  @Override
  public Optional<Store> findByCode(long code) {
    return storeRepositoryPort.findByCode(code)
      .map(mapper::toModel);
  }

  @Override
  public Page<Store> findAll(int page, int size) {
    return storeRepositoryPort.findAll(PageRequest.of(page, size))
      .map(mapper::toModel);
  }

  @Override
  public boolean existsByCode(long code) {
    return storeRepositoryPort.existsById(code);
  }
}
