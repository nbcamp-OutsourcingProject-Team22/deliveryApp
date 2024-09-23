package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderUserResponseDto;

public interface OrderService {
    // 주문 생성
    ApiResponse<OrderResponseDto> requestOrder(AuthMember authMember, OrderRequestDto orderRequestDto);
    // 주문 상태 조회
    ApiResponse<OrderUserResponseDto> checkOrder(AuthMember authMember, long orderId);
    // 주문 수락
    ApiResponse<OrderOwnerResponseDto> acceptOrder(AuthMember authMember, long orderId);
    // 주문 거절
    ApiResponse<OrderOwnerResponseDto> rejectOrder(AuthMember authMember, long orderId);
    // 주문 진행
    ApiResponse<OrderOwnerResponseDto> proceedOrder(AuthMember authMember, long orderId);

}
