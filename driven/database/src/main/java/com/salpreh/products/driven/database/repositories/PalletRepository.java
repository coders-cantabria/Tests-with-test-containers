package com.salpreh.products.driven.database.repositories;

import com.salpreh.products.driven.database.models.PalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalletRepository extends JpaRepository<PalletEntity, String> {
}
