package com.salpreh.products.application.usecases.tests.utils;

import java.io.InputStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Scripts {

  private static final String ROOT = "dbscripts/";
  public static final String DATA_ALL = ROOT + "data-all.sql";
  public static final String DELETE_ALL = ROOT + "delete-all.sql";

  public static String loadScript(String script) {
    InputStream is = Scripts.class.getClassLoader().getResourceAsStream(script);
    try {
      return new String(is.readAllBytes());
    } catch (Exception e) {
      throw new RuntimeException("Error loading data from: " + script, e);
    }
  }
}
