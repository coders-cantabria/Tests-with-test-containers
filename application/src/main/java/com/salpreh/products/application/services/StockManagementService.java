package com.salpreh.products.application.services;

import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.application.ports.driven.StoreRepositoryPort;
import com.salpreh.products.application.ports.driven.StoreStockRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockManagementService {

  private final StoreStockRepositoryPort storeStockRepositoryPort;
  private final StoreRepositoryPort storeRepositoryPort;
  private final ProductRepositoryPort productRepositoryPort;

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener
  public void managePalletStock(StockUpdateEvent stockUpdate) {
    if (!isValid(stockUpdate)) return;

    StoreStock stock = storeStockRepositoryPort.findById(stockUpdate.getStoreCode(), stockUpdate.getProductBarcode())
      .orElseGet(() -> StoreStock.of(stockUpdate.getStoreCode(), stockUpdate.getProductBarcode()));

    stock.addQuantity(stockUpdate.getQuantity());
    storeStockRepositoryPort.upsert(stock);
  }

  private boolean isValid(StockUpdateEvent palletCreated) {
    if (!productRepositoryPort.existsByBarcode(palletCreated.getProductBarcode())) {
      log.warn("Pallet with non existing product ({}). Stock update skipped.", palletCreated.getProductBarcode());
      return false;
    }
    if (!storeRepositoryPort.existsByCode(palletCreated.getStoreCode())) {
      log.warn("Pallet with non existing store ({}). Stock update skipped.", palletCreated.getStoreCode());
      return false;
    }

    return true;
  }
}
