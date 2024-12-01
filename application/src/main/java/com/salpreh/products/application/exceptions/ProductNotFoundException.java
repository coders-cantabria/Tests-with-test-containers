package com.salpreh.products.application.exceptions;

import com.salpreh.products.application.exceptions.base.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

  private static final String MSG = "Product with barcode %s not found";

  public ProductNotFoundException(String barcode) {
    super(String.format(MSG, barcode));
  }
}
