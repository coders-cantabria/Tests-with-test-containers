package com.salpreh.products.application.usecases;

import com.salpreh.products.application.mapperes.DomainEventMapper;
import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.models.commons.IdName;
import com.salpreh.products.application.ports.driven.PalletRepositoryPort;
import com.salpreh.products.application.ports.driving.PalletUseCasePort;
import com.salpreh.products.application.services.Ean128Decoder;
import com.salpreh.products.application.services.StockManagementService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PalletUseCase implements PalletUseCasePort {

  private final Ean128Decoder eanDecoder;
  private final StockManagementService stockManagementService;
  private final PalletRepositoryPort palletRepositoryPort;
  private final DomainEventMapper eventMapper;

  @Override
  @Transactional(readOnly = true)
  public Pallet decodeEan128(String ean) {
    return eanDecoder.decodeEan128(ean);
  }

  @Override
  @Transactional
  public Pallet createPallet(String ean) {
    Pallet pallet = eanDecoder.decodeEan128(ean);

    if (palletRepositoryPort.existsById(pallet.getId()))
      throw new IllegalArgumentException("Pallet already exists");

    palletRepositoryPort.create(pallet);
    stockManagementService.publishStockUpdate(eventMapper.mapToEvent(pallet));

    return pallet;
  }

  @Override
  @Transactional
  public Pallet createPallet(Pallet pallet) {
    validateIncomingPallet(pallet);

    Optional<Pallet> existingPallet = palletRepositoryPort.findById(pallet.getId());
    if (existingPallet.isPresent()) {
      log.warn("Attempt to register an existing pallet: {}. Skipping creation", pallet.getId());
      return existingPallet.get();
    }

    palletRepositoryPort.create(pallet);
    stockManagementService.publishStockUpdate(eventMapper.mapToEvent(pallet));

    return pallet;
  }

  /**
   * Method to validate {@link Pallet} data when we not are decoding it from an EAN128 code.
   *
   * @param pallet
   */
  private void validateIncomingPallet(Pallet pallet) {
    if (pallet.getId() == null) {
      throw new IllegalArgumentException("Invalid pallet");
    }

    if (!IdName.validId(pallet.getProduct()) || !eanDecoder.validProduct(pallet.getProduct().getId())) {
      throw new IllegalArgumentException("Invalid product");
    }

    if (!IdName.validId(pallet.getSupplier()) || !eanDecoder.validSupplier(pallet.getSupplier().getId())) {
      throw new IllegalArgumentException("Invalid supplier");
    }

    if (!IdName.validId(pallet.getStore()) || !eanDecoder.validStore(pallet.getStore().getId())) {
      throw new IllegalArgumentException("Invalid store");
    }

    if (pallet.getUnits() == null || pallet.getUnits() <= 0) {
      throw new IllegalArgumentException("Invalid units");
    }
  }

}
