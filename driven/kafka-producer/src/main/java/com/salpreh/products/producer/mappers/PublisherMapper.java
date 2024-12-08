package com.salpreh.products.producer.mappers;

import com.salpreh.products.application.models.events.StockUpdateEvent;
import com.salpreh.products.producer.models.ExternalStockUpdateEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

  ExternalStockUpdateEvent toExternalEvent(StockUpdateEvent src);
}
