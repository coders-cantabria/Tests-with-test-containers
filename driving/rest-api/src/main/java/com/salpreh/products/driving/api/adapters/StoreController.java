package com.salpreh.products.driving.api.adapters;

import com.salpreh.products.application.models.Store;
import com.salpreh.products.application.ports.driving.StoreReadUseCasePort;
import com.salpreh.products.driving.api.mappers.ApiMapper;
import com.salpreh.products.driving.api.models.ApiPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreReadUseCasePort storeReadUseCase;
  private final ApiMapper mapper;

  @GetMapping("/{storeCode}")
  public ResponseEntity<Store> getStore(@PathVariable long storeCode) {
    return storeReadUseCase.getStore(storeCode)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<ApiPage<Store>> getStores(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    var stores = mapper.toApiPage(storeReadUseCase.getStores(page, size));

    return ResponseEntity.ok(stores);
  }
}
