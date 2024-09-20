package com.sparta.deliveryapp.domain.store.model;

import com.sparta.deliveryapp.entity.Store;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 단건 가게 조회
 */
@AllArgsConstructor
public class StoreResponseDto {
    private Long id;
    private String storeName;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer minOrderAmount;
    private boolean isClose;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // TODO: 메뉴 목록들 볼 수 있어야함

    // Dto -> Entity
    public StoreResponseDto of(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getMinOrderAmount(),
                store.isClose(),
                store.getCreatedAt(),
                store.getUpdatedAt()
        );
    }
}
