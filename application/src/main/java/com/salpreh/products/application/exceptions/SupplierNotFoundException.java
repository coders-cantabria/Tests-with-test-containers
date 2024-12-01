package com.salpreh.products.application.exceptions;

import com.salpreh.products.application.exceptions.base.NotFoundException;

public class SupplierNotFoundException extends NotFoundException {

  private static final String MSG = "Supplier with id %d not found";

  public SupplierNotFoundException(Long id) {
    super(String.format(MSG, id));
  }
}
