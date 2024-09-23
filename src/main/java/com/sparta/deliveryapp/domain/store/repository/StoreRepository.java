package com.sparta.deliveryapp.domain.store.repository;

import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    /**
     * 가게이름과 일부 맞는걸 찾고, 폐업되지 않은 가게들만 찾아오는 JPA 쿼리
     * @param storeName 가게 이름
     * @param pageable 페이지 정보
     * @return 페이지객체에 담긴 가게 반환
     */
    Page<Store> findAllByStoreNameContainingAndIsCloseFalse(String storeName, Pageable pageable);
    /**
     * 모든 가게를 찾는데, 폐업되지 않은 가게들만 찾아오는 JPA 쿼리
     * @param pageable 페이지 정보
     * @return 페이지객체에 담긴 가게 반환
     */
    Page<Store> findAllByIsCloseFalse(Pageable pageable);

    /**
     * 가게 이름하고 정확히 일치하는 가게 반환
     * @param storeName 찾을 가게 이름
     * @return 가게 반환
     */
    Optional<Store> findByStoreName(String storeName);

    List<Store> findAllByMemberAndIsCloseFalse(Member member);

}
