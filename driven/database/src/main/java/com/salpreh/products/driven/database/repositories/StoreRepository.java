package com.salpreh.products.driven.database.repositories;

import com.salpreh.products.driven.database.models.StoreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
  Optional<StoreEntity> findByCode(long code);
}
