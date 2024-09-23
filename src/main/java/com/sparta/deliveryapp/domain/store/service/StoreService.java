package com.sparta.deliveryapp.domain.store.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.model.StoreResponseDto;
import com.sparta.deliveryapp.domain.store.model.StoresResponseDto;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    ApiResponse<Void> createStore(Long memberId, StoreRequestDto requestDto);
    ApiResponse<Void> updateStore(Long memberId, Long storeId, StoreRequestDto storeRequestDto);
    ApiResponse<StoreResponseDto> getStore(String storeName);
    ApiResponse<List<StoresResponseDto>> getStores(String storeName, Pageable pageable);
    ApiResponse<Void> closeStore(Long memberId, Long storeId);
    Store findByStoreId(Long storeId);
    Store findByStoreName(String storeName);
    Page<Store> findAllByStoreNameAndPage(String storeName, Pageable pageable);
    Page<Store> findAllStorePage(Pageable pageable);
    Integer getStoreCount(Member member);

}
