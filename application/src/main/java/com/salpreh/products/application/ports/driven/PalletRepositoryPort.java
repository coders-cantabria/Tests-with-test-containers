package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.Pallet;

public interface PalletRepositoryPort {
  boolean existsById(String id);
  Pallet create(Pallet pallet);
}
