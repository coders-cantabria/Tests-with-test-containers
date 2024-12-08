package com.salpreh.products.driving.consumer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalStockUpdateEvent {
  private String originSystemId;

  private String productId;
  private Long storeId;

  @Builder.Default
  private Integer quantity = 0;
}
