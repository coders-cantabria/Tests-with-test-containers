package com.salpreh.products.application.models.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class IdName<T> {
  public T id;
  public String name;

  public static boolean validId(IdName<?> idName) {
    return idName != null && idName.id != null;
  }
}
