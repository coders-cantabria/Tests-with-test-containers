package com.salpreh.products.driven.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "addresses")
public class AddressEntity {

  @Id
  @GeneratedValue(generator = "address_id_seq")
  @SequenceGenerator(name = "address_id_seq", sequenceName = "address_id_seq", allocationSize = 1)
  private Long id;

  private String street;
  private String city;
  private String zipCode;
  private String countryCode;

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof AddressEntity)) return false;

      return id != null && id.equals(((AddressEntity) o).getId());
  }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }
}
