package com.salpreh.products.driven.database.repositories;

import com.salpreh.products.driven.database.models.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
}
