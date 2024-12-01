package com.salpreh.products.application.usecases;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;
import com.salpreh.products.application.ports.driven.ProductRepositoryPort;
import com.salpreh.products.application.ports.driving.ProductWriteUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductWriteUseCase implements ProductWriteUseCasePort {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional
  public Product create(UpsertProductCommand createCommand) {
    return productRepositoryPort.create(createCommand);
  }

  @Override
  @Transactional
  public Product update(String barcode, UpsertProductCommand updateCommand) {
    return productRepositoryPort.update(barcode, updateCommand);
  }

  @Override
  @Transactional
  public void delete(String barcode) {
    productRepositoryPort.delete(barcode);
  }
}
