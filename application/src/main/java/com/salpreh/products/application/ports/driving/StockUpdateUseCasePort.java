package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.events.StockUpdateEvent;

public interface StockUpdateUseCasePort {

    void processStockUpdate(StockUpdateEvent stockUpdate);
}
