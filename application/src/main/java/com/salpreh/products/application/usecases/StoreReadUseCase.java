package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.Store;
import com.salpreh.products.application.ports.driven.StoreRepositoryPort;
import com.salpreh.products.application.ports.driving.StoreReadUseCasePort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreReadUseCase implements StoreReadUseCasePort {

  private final StoreRepositoryPort storeRepositoryPort;


  @Override
  @Transactional(readOnly = true)
  public Page<Store> getStores(int page, int size) {
    return storeRepositoryPort.findAll(page, size);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Store> getStore(long code) {
    return storeRepositoryPort.findByCode(code);
  }
}
