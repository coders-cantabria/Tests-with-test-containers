package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.application.ports.driving.StockUpdateUseCasePort;
import com.salpreh.products.application.services.StockManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockUpdateUseCase implements StockUpdateUseCasePort {

  private final StockManagementService stockManagementService;

  @Override
  public void processStockUpdate(StockUpdateEvent stockUpdate) {
    stockManagementService.manageStockUpdate(stockUpdate);
  }
}
