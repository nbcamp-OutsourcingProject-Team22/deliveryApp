package com.sparta.deliveryapp.domain.store.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * 사장이 가게 생성할때 기재해야할 항목들
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StoreRequestDto {
    @NotNull(message = "가게이름은 공백 일 수 없습니다")
    private String storeName;
    @NotNull(message = "오픈시간은 공백 일 수 없습니다")
    private LocalTime openingTime;
    @NotNull(message = "마감시간은 공백 일 수 없습니다")
    private LocalTime closingTime;
    @NotNull(message = "최소주문금액은 공백 일 수 없습니다")
    private Integer minOrderAmount;
}
