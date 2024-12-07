package com.salpreh.products.driven.database.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "products")
public class ProductEntity {

  @Id
  private String barcode;
  private String name;
  private String description;
  private String imageUrl;
  private double purchasePrice;
  private double sellingPrice;

  @ManyToMany(cascade = {CascadeType.REFRESH})
  @JoinTable(
    name = "products_suppliers",
    joinColumns = @JoinColumn(name = "product_barcode", referencedColumnName = "barcode"),
    inverseJoinColumns = @JoinColumn(name = "supplier_id", referencedColumnName = "id")
  )
  public Set<SupplierEntity> suppliers = new HashSet<>();

  @ElementCollection
  @JoinTable(name = "products_tags")
  public List<String> tags = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = {CascadeType.REFRESH}, orphanRemoval = true)
  public List<StoreStockEntity> stock = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ProductEntity)) return false;

      return barcode != null && barcode.equals(((ProductEntity) o).getBarcode());
  }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }
}
