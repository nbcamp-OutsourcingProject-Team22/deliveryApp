package com.sparta.deliveryapp.domain.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long storeId;
    private Long menuId;
}
