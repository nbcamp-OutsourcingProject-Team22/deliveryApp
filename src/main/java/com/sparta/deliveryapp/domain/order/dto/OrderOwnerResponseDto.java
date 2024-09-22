package com.sparta.deliveryapp.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderOwnerResponseDto {
    private Long storeId;
    private String process;
}
