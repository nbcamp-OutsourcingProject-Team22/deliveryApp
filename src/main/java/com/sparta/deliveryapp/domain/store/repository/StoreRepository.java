package com.sparta.deliveryapp.domain.store.repository;

import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllByStoreNameContaining(String storeName, Pageable pageable);
    Page<Store> findAll(Pageable pageable);
    Optional<Store> findByStoreName(String storeName);
}
