package com.salpreh.products.application.models;

import java.util.List;

public record Product(
  String barcode,
  String name,
  String description,
  String imageUrl,
  double purchasePrice,
  double sellingPrice,
  List<Supplier> suppliers,
  List<String> tags
) {

}
