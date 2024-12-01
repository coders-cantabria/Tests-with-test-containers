package com.salpreh.products.application.models;

import java.util.List;

public record Store(Long code, String name, List<StoreStock> stock) {}
