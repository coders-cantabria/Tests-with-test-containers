package com.salpreh.products.application.models.commons;

import java.util.List;
import lombok.Value;

@Value(staticConstructor = "of")
public class Range<T> {
  private final T min;
  private final T max;

  public static <T> Range of(List<T> src) {
    if (src == null) return null;
    if (src.size() != 2) throw new IllegalArgumentException("Expected collection with 2 items for range");

    return Range.of(src.get(0), src.get(1));
  }
}
