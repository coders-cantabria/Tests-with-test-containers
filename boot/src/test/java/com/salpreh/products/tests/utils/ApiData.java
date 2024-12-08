package com.salpreh.products.tests.utils;

import java.io.InputStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiData {

  private static final String ROOT = "apidata/";
  public static final String POST_PRODUCT = ROOT + "product-post.json";
  public static final String POST_PRODUCT_EXISTS = ROOT + "product-post-exists.json";
  public static final String PUT_PRODUCT = ROOT + "product-put.json";

  public static String loadApiData(String script) {
    InputStream is = Scripts.class.getClassLoader().getResourceAsStream(script);
    try {
      return new String(is.readAllBytes());
    } catch (Exception e) {
      throw new RuntimeException("Error loading data from: " + script, e);
    }
  }
}
