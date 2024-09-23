package com.sparta.deliveryapp.domain.order.controller;

import com.sparta.deliveryapp.annotation.Auth;
import com.sparta.deliveryapp.annotation.TrackOrder;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderUserResponseDto;
import com.sparta.deliveryapp.domain.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class OrderUserController {

    private final OrderServiceImpl orderServiceImpl;

    @TrackOrder
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<OrderResponseDto>> requestOrder(@Auth AuthMember member, @RequestBody OrderRequestDto orderRequestDto){

        ApiResponse<OrderResponseDto> response = orderServiceImpl.requestOrder(member, orderRequestDto);
        return ApiResponse.of(response);
    }

    @TrackOrder
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderUserResponseDto>> checkOrder(@Auth AuthMember member,@PathVariable long orderId){

        ApiResponse<OrderUserResponseDto> response = orderServiceImpl.checkOrder(member, orderId);
        return ApiResponse.of(response);
    }


}
