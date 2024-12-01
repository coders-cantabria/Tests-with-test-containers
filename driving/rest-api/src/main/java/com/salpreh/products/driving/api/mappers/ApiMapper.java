package com.salpreh.products.driving.api.mappers;

import com.salpreh.products.driving.api.models.ApiPage;
import com.salpreh.products.driving.api.models.ApiPage.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ApiMapper {

  default <T> ApiPage<T> toApiPage(Page<T> src) {
    return ApiPage.<T>builder()
      .data(src.getContent())
      .page(toPageInfo(src))
      .build();
  }

  @Named("toPageInfo")
  default PageInfo toPageInfo(Page<?> src) {
    return PageInfo.builder()
      .requestedPage(src.getNumber())
      .numElements(src.getNumberOfElements())
      .totalPages(src.getTotalPages())
      .totalElements(src.getTotalElements())
      .build();
  }
}
