package com.salpreh.products.driven.database.mappers;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.Supplier;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.driven.database.models.ProductEntity;
import com.salpreh.products.driven.database.models.SupplierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product toModel(ProductEntity src);
  ProductEntity toEntity(Product src);

  @Mapping(target = "suppliers", ignore = true)
  ProductEntity toEntity(UpsertProductCommand src);
  @Mapping(target = "suppliers", ignore = true)
  @Mapping(target = "barcode", ignore = true)
  void toEntity(@MappingTarget ProductEntity target, UpsertProductCommand src);

  Supplier toModel(SupplierEntity src);
}
