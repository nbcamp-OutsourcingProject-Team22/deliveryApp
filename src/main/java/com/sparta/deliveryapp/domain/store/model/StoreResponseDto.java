package com.sparta.deliveryapp.domain.store.model;

import com.sparta.deliveryapp.domain.menu.dto.MenuResponse;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 단건 가게 조회
 */
@Getter
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
    private MenuResponse menu;

    // Dto -> Entity
    public static StoreResponseDto of(Store store, Menu menu) {
        MenuResponse menuResponse = new MenuResponse(menu);
        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getMinOrderAmount(),
                store.isClose(),
                store.getCreatedAt(),
                store.getUpdatedAt(),
                menuResponse
        );
    }
}
