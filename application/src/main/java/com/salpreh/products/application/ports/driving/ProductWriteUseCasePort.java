package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.Product;
import com.salpreh.products.application.models.commands.UpsertProductCommand;

public interface ProductWriteUseCasePort {
  Product create(UpsertProductCommand createCommand);
  Product update(String barcode, UpsertProductCommand updateCommand);
  void delete(String barcode);
}
