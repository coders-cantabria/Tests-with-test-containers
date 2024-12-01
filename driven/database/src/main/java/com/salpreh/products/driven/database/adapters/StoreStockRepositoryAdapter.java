package com.salpreh.products.driven.database.adapters;

import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.application.ports.driven.StoreStockRepositoryPort;
import com.salpreh.products.driven.database.mappers.StoreMapper;
import com.salpreh.products.driven.database.models.StoreStockEntity;
import com.salpreh.products.driven.database.models.StoreStockEntity.StoreStockPk;
import com.salpreh.products.driven.database.repositories.StoreStockRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreStockRepositoryAdapter implements StoreStockRepositoryPort {

  private final StoreStockRepository storeStockRepository;
  private final StoreMapper mapper;

  @Override
  public Optional<StoreStock> findById(Long storeCode, String productBarcode) {
    return storeStockRepository.findById(StoreStockPk.of(storeCode, productBarcode))
      .map(mapper::toModel);
  }

  @Override
  public StoreStock upsert(StoreStock storeStock) {
    StoreStockEntity stockData = mapper.toEntity(storeStock);
    stockData = storeStockRepository.save(stockData);

    return mapper.toModel(stockData);
  }
}
