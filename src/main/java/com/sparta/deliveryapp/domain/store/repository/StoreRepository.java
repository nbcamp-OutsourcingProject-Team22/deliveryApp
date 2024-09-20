package com.sparta.deliveryapp.domain.store.repository;

import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
