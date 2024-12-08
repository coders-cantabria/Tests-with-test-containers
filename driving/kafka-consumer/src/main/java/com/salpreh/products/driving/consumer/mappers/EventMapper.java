package com.salpreh.products.driving.consumer.mappers;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.driving.consumer.models.ExternalPalletUnloadEvent;
import com.salpreh.products.driving.consumer.models.ExternalStockUpdateEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

  StockUpdateEvent toDomain(ExternalStockUpdateEvent src);

  @Mapping(target = "id", source = "event.globalId")
  @Mapping(target = "product.id", source = "event.productId")
  @Mapping(target = "store.id", source = "event.destinationId")
  @Mapping(target = "batchId", source = "event.batchId")
  @Mapping(target = "productionDate", source = "event.productionDate")
  @Mapping(target = "units", source = "event.units")
  @Mapping(target = "supplier.id", source = "supplierId")
  Pallet toDomain(String supplierId, ExternalPalletUnloadEvent event);
}
