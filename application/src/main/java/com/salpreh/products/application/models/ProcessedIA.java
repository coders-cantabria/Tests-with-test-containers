package com.salpreh.products.application.models;

import com.salpreh.products.application.models.constants.Ean128IA;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProcessedIA {
  private final Ean128IA ia;
  private final int newIndex;
  private final String data;

  public static ProcessedIA create(Ean128IA ia, int newIndex, String data) {
    return ProcessedIA.builder()
      .ia(ia)
      .newIndex(newIndex)
      .data(data)
      .build();
  }

  public static ProcessedIA unprocessed() {
    return ProcessedIA.builder().build();
  }

  public boolean isProcessed() {
    return ia != null;
  }
}
