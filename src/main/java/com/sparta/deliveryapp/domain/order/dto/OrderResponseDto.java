package com.sparta.deliveryapp.domain.order.dto;

import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
}
