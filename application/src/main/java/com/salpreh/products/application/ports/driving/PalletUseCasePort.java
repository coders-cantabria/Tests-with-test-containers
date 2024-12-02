package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.Pallet;

public interface PalletUseCasePort {

  Pallet decodeEan128(String ean);
  Pallet createPallet(String ean);
}
