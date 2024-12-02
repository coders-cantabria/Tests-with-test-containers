package com.salpreh.products.application.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreStock {

  private Long storeCode;
  private String productBarcode;
  @Builder.Default
  private long quantity = 0;

  public static StoreStock of(Long storeCode, String productBarcode) {
    return StoreStock.builder()
        .storeCode(storeCode)
        .productBarcode(productBarcode)
        .build();
  }

  public void addQuantity(long quantity) {
    this.quantity += quantity;
  }

  public void subtractQuantity(long quantity) {
    this.quantity = Math.max(this.quantity - quantity, 0);
  }
}
