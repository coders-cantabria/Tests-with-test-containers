package com.salpreh.products.driving.consumer.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPalletUnloadEvent {

  private String globalId;
  private String originSystemId;

  private String productId;
  private Long destinationId;
  private String batchId;
  private LocalDate productionDate;
  private Integer units;
}
