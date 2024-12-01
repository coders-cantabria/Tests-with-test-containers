package com.salpreh.products.driven.database.adapters;

import com.salpreh.products.application.models.Pallet;
import com.salpreh.products.application.ports.driven.PalletRepositoryPort;
import com.salpreh.products.driven.database.mappers.PalletMapper;
import com.salpreh.products.driven.database.models.PalletEntity;
import com.salpreh.products.driven.database.repositories.PalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PalletRepositoryAdapter implements PalletRepositoryPort {

  private final PalletRepository palletRepository;
  private final PalletMapper mapper;

  @Override
  public boolean existsById(String id) {
    return palletRepository.existsById(id);
  }

  @Override
  public Pallet create(Pallet pallet) {
    PalletEntity palletData = mapper.toEntity(pallet);
    palletRepository.save(palletData);

    return pallet;
  }
}
