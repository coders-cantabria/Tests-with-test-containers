package com.salpreh.products.driven.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "stores")
public class StoreEntity {

  @Id
  private Long code;
  private String name;

  @OneToMany(mappedBy = "store")
  private List<StoreStockEntity> stock = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof StoreEntity)) return false;

      return code != null && code.equals(((StoreEntity) o).getCode());
  }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }
}
