package com.salpreh.products.driven.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "pallets")
public class PalletEntity {

  @Id
  private String id;

  @Column(name = "product_barcode")
  private String productId;
  @Column(name = "store_code")
  private Long storeId;
  private Long supplierId;

  private String batchId;
  private LocalDate productionDate;
  private Integer units;

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof PalletEntity)) return false;

      return id != null && id.equals(((PalletEntity) o).getId());
  }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }
}
