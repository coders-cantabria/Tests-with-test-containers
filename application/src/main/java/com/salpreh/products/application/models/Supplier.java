package com.salpreh.products.application.models;

public record Supplier(Long id, String name, String iban, Address billingAddress) {

  public record Address(String street, String city, String zipCode, String countryCode) {}
}
