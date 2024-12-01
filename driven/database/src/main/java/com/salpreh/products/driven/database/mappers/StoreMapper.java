package com.salpreh.products.driven.database.mappers;

import com.salpreh.products.application.models.Store;
import com.salpreh.products.application.models.StoreStock;
import com.salpreh.products.driven.database.models.StoreEntity;
import com.salpreh.products.driven.database.models.StoreStockEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {
  Store toModel(StoreEntity src);
  StoreEntity toEntity(Store src);

  @Mapping(target = "productBarcode", source = "id.productBarcode")
  @Mapping(target = "storeCode", source = "id.storeCode")
  StoreStock toModel(StoreStockEntity src);

  @InheritInverseConfiguration
  StoreStockEntity toEntity(StoreStock src);
}
