package com.salpreh.products.driven.database.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "suppliers")
public class SupplierEntity {

  @Id
  private Long id;
  private String name;
  private String iban;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
  @JoinColumn(name = "billing_address_id")
  private AddressEntity billingAddress;

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof SupplierEntity)) return false;

      return id != null && id.equals(((SupplierEntity) o).getId());
  }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }
}
