package com.salpreh.products.application.mapperes;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.events.StockUpdateEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DomainEventMapper {

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "storeId", source = "store.id")
  @Mapping(target = "quantity", source = "units")
  StockUpdateEvent mapToEvent(Pallet src);
}
