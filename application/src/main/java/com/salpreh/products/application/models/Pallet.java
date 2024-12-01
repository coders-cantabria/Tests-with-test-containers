package com.salpreh.products.application.models;

import com.salpreh.products.application.models.commons.IdName;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pallet {
  private String id;
  private IdName<String> product;
  private IdName<Long> supplier;
  private IdName<Long> store;
  private String batchId;
  private LocalDate productionDate;
  private Integer units;
}
