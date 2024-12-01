package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.StoreStock;
import java.util.Optional;

public interface StoreStockRepositoryPort {
  Optional<StoreStock> findById(Long storeCode, String productBarcode);

  StoreStock upsert(StoreStock storeStock);
}
