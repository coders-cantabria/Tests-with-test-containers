package com.salpreh.products.driven.database.repositories;

import com.salpreh.products.driven.database.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>,
  JpaSpecificationExecutor<ProductEntity> { }
