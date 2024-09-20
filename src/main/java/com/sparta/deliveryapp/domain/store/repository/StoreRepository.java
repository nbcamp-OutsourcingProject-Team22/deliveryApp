package com.sparta.deliveryapp.domain.store.repository;

import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
//    List<Store> findALlByMemberId(Long memberId);

}
