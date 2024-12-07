package com.salpreh.products.application.usecases;

import com.salpreh.products.application.mapperes.DomainEventMapper;
import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.ports.driven.PalletRepositoryPort;
import com.salpreh.products.application.ports.driving.PalletUseCasePort;
import com.salpreh.products.application.services.Ean128Decoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PalletUseCase implements PalletUseCasePort {

  private final Ean128Decoder eanDecoder;
  private final PalletRepositoryPort palletRepositoryPort;
  private final DomainEventMapper eventMapper;

  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional(readOnly = true)
  public Pallet decodeEan128(String ean) {
    return eanDecoder.decodeEan128(ean);
  }

  @Override
  @Transactional
  public Pallet createPallet(String ean) {
    Pallet pallet = eanDecoder.decodeEan128(ean);

    if (palletRepositoryPort.existsById(pallet.getId())) {
      throw new IllegalArgumentException("Pallet already exists");
    }
    palletRepositoryPort.create(pallet);

    eventPublisher.publishEvent(eventMapper.mapToEvent(pallet));

    return pallet;
  }

}
