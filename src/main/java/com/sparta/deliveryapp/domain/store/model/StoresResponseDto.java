package com.sparta.deliveryapp.domain.store.model;

import com.sparta.deliveryapp.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 다건 가게 조회
 */
@Getter
@AllArgsConstructor
public class StoresResponseDto {
    private Long id;
    private String storeName;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer minOrderAmount;
    private boolean isClose;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dto -> Entity
    public static StoresResponseDto of(Store store) {
        return new StoresResponseDto(
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
