package com.salpreh.products.driven.database.mappers;

import com.salpreh.products.application.models.Supplier;
import com.salpreh.products.driven.database.models.SupplierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

  Supplier toModel(SupplierEntity src);
}
