package com.salpreh.products.application.services;

import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.application.ports.driven.StoreRepositoryPort;
import com.salpreh.products.application.ports.driven.StoreStockRepositoryPort;
import com.salpreh.products.application.ports.driving.StockUpdatePublisherPort;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockManagementService {

  private final StoreStockRepositoryPort storeStockRepositoryPort;
  private final StoreRepositoryPort storeRepositoryPort;
  private final ProductRepositoryPort productRepositoryPort;
  private final StockUpdatePublisherPort stockUpdatePublisherPort;

  public void publishStockUpdate(StockUpdateEvent stockUpdate) {
    stockUpdatePublisherPort.publishStockUpdate(stockUpdate);
  }

  public void processStockUpdate(StockUpdateEvent stockUpdate) {
    if (!isValid(stockUpdate)) return;

    // Simulate a long-running process
    updateForecastCalculations(stockUpdate);

    StoreStock stock = storeStockRepositoryPort.findById(stockUpdate.getStoreCode(), stockUpdate.getProductBarcode())
      .orElseGet(() -> StoreStock.of(stockUpdate.getStoreCode(), stockUpdate.getProductBarcode()));

    stock.addQuantity(stockUpdate.getQuantity());
    storeStockRepositoryPort.upsert(stock);
  }

  private boolean isValid(StockUpdateEvent stockUpdate) {
    if (!productRepositoryPort.existsByBarcode(stockUpdate.getProductBarcode())) {
      log.warn("Event with non existing product ({}). Stock update skipped.", stockUpdate.getProductBarcode());
      return false;
    }
    if (!storeRepositoryPort.existsByCode(stockUpdate.getStoreCode())) {
      log.warn("Event with non existing store ({}). Stock update skipped.", stockUpdate.getStoreCode());
      return false;
    }

    return true;
  }

  private void updateForecastCalculations(StockUpdateEvent stockUpdate) {
    try {
      Thread.sleep(Duration.ofSeconds(3).toMillis());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
