package com.salpreh.products.driven.database.repositories.filtering;

import static com.salpreh.products.driven.database.utils.QueryUtils.likeStatement;

import com.salpreh.products.application.models.commons.Range;
import com.salpreh.products.application.models.filters.ProductFilter;
import com.salpreh.products.driven.database.constants.DbFunctions;
import com.salpreh.products.driven.database.models.ProductEntity;
import com.salpreh.products.driven.database.models.ProductEntity_;
import com.salpreh.products.driven.database.models.SupplierEntity;
import com.salpreh.products.driven.database.models.SupplierEntity_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ProductFiltering {

  public static Specification<ProductEntity> process(ProductFilter filter) {
    Specification<ProductEntity> spec = empty();

    if (filter.hasSearch()) spec = spec.and(containsText(filter.getSearch()));
    if (filter.hasPurchasePriceRange()) spec = spec.and(purchasePriceBetween(filter.getPurchasePriceRange()));
    if (filter.hasSellingPriceRange()) spec = spec.and(sellingPriceBetween(filter.getSellingPriceRange()));
    if (filter.hasTags()) spec = spec.and(hasTags(filter.getTags()));
    if (filter.hasSupplierIds()) spec = spec.and(hasSupplierIds(filter.getSupplierIds()));

    return spec;
  }

  public static Specification<ProductEntity> containsText(String search) {
    return (root, query, cb) ->
      cb.or(
        cb.like(normalizeField(cb, root.get(ProductEntity_.name)), likeStatement(search)),
        cb.like(normalizeField(cb, root.get(ProductEntity_.description)), likeStatement(search))
      );
  }

  public static Specification<ProductEntity> purchasePriceBetween(Range<Double> range) {
    return (root, query, cb) ->
      cb.between(root.get(ProductEntity_.purchasePrice), range.getMin(), range.getMax());
  }

  public static Specification<ProductEntity> sellingPriceBetween(Range<Double> range) {
    return (root, query, cb) ->
      cb.between(root.get(ProductEntity_.sellingPrice), range.getMin(), range.getMax());
  }

  public static Specification<ProductEntity> hasTags(Set<String> tags) {
    return (root, query, cb) -> {
      Join<ProductEntity, String> tagsJoin = (Join<ProductEntity, String>) root.fetch(ProductEntity_.tags);

      return tagsJoin.in(tags);
    };
  }

  public static Specification<ProductEntity> hasSupplierIds(Set<Long> supplierIds) {
    return (root, query, cb) -> {
       Join<ProductEntity, SupplierEntity> suppliersJoin = (Join<ProductEntity, SupplierEntity>) root.fetch(ProductEntity_.suppliers);
       suppliersJoin.fetch(SupplierEntity_.billingAddress);

       return suppliersJoin.get(SupplierEntity_.id).in(supplierIds);
    };
  }

  private static Specification<ProductEntity> empty() {
    return (root, query, cb) -> {
      Join<ProductEntity, SupplierEntity> suppliersJoin = (Join<ProductEntity, SupplierEntity>) root.fetch(ProductEntity_.suppliers);
      suppliersJoin.fetch(SupplierEntity_.billingAddress);
      root.fetch(ProductEntity_.tags);

      return cb.and();
    };
  }

  private static Expression<String> normalizeField(CriteriaBuilder cb, Expression<String> field) {
    return cb.lower(cb.function(DbFunctions.UNACCENT, String.class, field));
  }
}
