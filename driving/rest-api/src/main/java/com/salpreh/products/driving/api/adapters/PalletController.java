package com.salpreh.products.driving.api.adapters;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.ports.driving.PalletUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pallet")
@RequiredArgsConstructor
public class PalletController {

  private final PalletUseCasePort palletUseCase;

  @GetMapping("/decode")
  public ResponseEntity<Pallet> decodeEan128(@RequestParam String eanCode) {
    return ResponseEntity.ok(palletUseCase.decodeEan128(eanCode));
  }

  @PostMapping
  public ResponseEntity<Pallet> createPallet(@RequestParam String eanCode) {
    return ResponseEntity.ok(palletUseCase.createPallet(eanCode));
  }
}
