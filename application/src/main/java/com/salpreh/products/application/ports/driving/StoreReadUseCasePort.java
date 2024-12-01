package com.salpreh.products.application.ports.driving;

import com.salpreh.products.application.models.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StoreReadUseCasePort {
    Page<Store> getStores(int page, int size);
    Optional<Store> getStore(long code);
}
