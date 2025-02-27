package com.sparta.deliveryapp.domain.store.model;

import com.sparta.deliveryapp.domain.menu.dto.MenuResponse;
import com.sparta.deliveryapp.domain.review.dto.ReviewResponseDto;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Review;
import com.sparta.deliveryapp.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private List<MenuResponse> menu;
    private List<ReviewResponseDto> review;

    public StoreResponseDto(Long id, String storeName, LocalTime openingTime, LocalTime closingTime, Integer minOrderAmount, boolean isClose, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minOrderAmount = minOrderAmount;
        this.isClose = isClose;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Dto -> Entity menu 있음
    public static StoreResponseDto of(Store store, List<Menu> menu, List<Review> review) {
        List<MenuResponse> menuResponse = menu.stream().map(MenuResponse::new).toList();
        List<ReviewResponseDto> reviewResponse = review.stream().map(ReviewResponseDto::from).toList();
        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getMinOrderAmount(),
                store.getIsClose(),
                store.getCreatedAt(),
                store.getUpdatedAt(),
                menuResponse,
                reviewResponse
        );
    }

    // Dto -> Entity menu 없음
    public static StoreResponseDto of(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getMinOrderAmount(),
                store.getIsClose(),
                store.getCreatedAt(),
                store.getUpdatedAt(),
                null,
                null
        );
    }
}
