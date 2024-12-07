package com.salpreh.products.application.models.filters;

import com.salpreh.products.application.models.commons.Range;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(staticName = "empty")
public class ProductFilter {
  private String search;
  private Range<Double> purchasePriceRange;
  private Range<Double> sellingPriceRange;
  private Set<String> tags;
  private Set<Long> supplierIds;

  public boolean hasSearch() {
    return search != null;
  }

  public boolean hasPurchasePriceRange() {
    return purchasePriceRange != null;
  }

  public boolean hasSellingPriceRange() {
    return sellingPriceRange != null;
  }

  public boolean hasTags() {
    return tags != null;
  }

  public boolean hasSupplierIds() {
    return supplierIds != null;
  }
}
