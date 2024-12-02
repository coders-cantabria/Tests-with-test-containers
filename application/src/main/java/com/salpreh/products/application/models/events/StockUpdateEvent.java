package com.salpreh.products.application.models.events;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StockUpdateEvent {
  private String productId;
  private long storeId;
  private int quantity;

  public Long getStoreCode() {
    return storeId;
  }

  public String getProductBarcode() {
    return productId;
  }

  public long getQuantity() {
    return quantity;
  }
}
