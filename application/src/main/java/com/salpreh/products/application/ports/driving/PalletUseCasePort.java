package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.Pallet;
import org.springframework.transaction.annotation.Transactional;

public interface PalletUseCasePort {

  Pallet decodeEan128(String ean);
  Pallet createPallet(String ean);
  Pallet createPallet(Pallet pallet);
}
