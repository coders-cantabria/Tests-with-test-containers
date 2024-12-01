package com.salpreh.products.application.models.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Ean128IA {
  PALLET_ID("00", 18, 18, true),
  PRODUCT_ID("01", 14, 14, true),
  SUPPLIER_ID("412", 13, 13, true),
  DELIVERY_SITE_ID("410", 13, 13, true),
  QUANTITY("37", 1, 8, true),
  BATCH_ID("10", 1, 20, false),
  PRODUCTION_DATE("11", 6, 6, false);

  private final String code;
  private final int minSize;
  private final int maxSize;
  private final boolean required;
  private static final Map<String, Ean128IA> eanByCode;
  static {
    eanByCode = Arrays.stream(Ean128IA.values())
      .collect(Collectors.toMap(Ean128IA::getCode, Function.identity()));
  }
  private static final Set<Ean128IA> requiredIas;
  static {
    requiredIas = Arrays.stream(Ean128IA.values())
      .filter(Ean128IA::isRequired)
      .collect(Collectors.toSet());
  }

  public boolean isVariable() {
    return minSize < maxSize;
  }

  public static Ean128IA fromCode(String code) {
    return eanByCode.get(code);
  }

  public static Set<Ean128IA> getRequiredIas() {
    return requiredIas;
  }
}
