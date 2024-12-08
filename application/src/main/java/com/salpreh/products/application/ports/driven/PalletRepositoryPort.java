package com.salpreh.products.application.ports.driven;

import com.salpreh.products.application.models.Pallet;
import java.util.Optional;

public interface PalletRepositoryPort {

  Optional<Pallet> findById(String id);
  boolean existsById(String id);
  Pallet create(Pallet pallet);
}
