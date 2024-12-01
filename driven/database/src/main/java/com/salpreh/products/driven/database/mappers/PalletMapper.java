package com.salpreh.products.driven.database.mappers;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.driven.database.models.PalletEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PalletMapper {

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "storeId", source = "store.id")
  @Mapping(target = "supplierId", source = "supplier.id")
  PalletEntity toEntity(Pallet pallet);

  @InheritInverseConfiguration
  Pallet toModel(PalletEntity entity);
}
