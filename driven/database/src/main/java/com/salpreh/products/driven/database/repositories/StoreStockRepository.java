package com.salpreh.products.driven.database.repositories;

import com.salpreh.products.driven.database.models.StoreStockEntity;
import com.salpreh.products.driven.database.models.StoreStockEntity.StoreStockPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreStockRepository extends JpaRepository<StoreStockEntity, StoreStockPk> {
}
