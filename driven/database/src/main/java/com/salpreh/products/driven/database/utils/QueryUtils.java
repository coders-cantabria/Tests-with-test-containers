package com.salpreh.products.driven.database.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryUtils {

  public static String likeStatement(String text) {
    return "%" + text.toLowerCase() + "%";
  }
}
