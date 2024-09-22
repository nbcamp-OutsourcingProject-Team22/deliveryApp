package com.sparta.deliveryapp.domain.order.controller;

import com.sparta.deliveryapp.annotation.TrackOrder;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import com.sparta.deliveryapp.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/orders")
@RequiredArgsConstructor
public class OrderOwnerController {

    private final OrderService orderService;

    @TrackOrder
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<ApiResponse<OrderOwnerResponseDto>> acceptOrder(@PathVariable long orderId){
        Member member = new Member();
        ApiResponse<OrderOwnerResponseDto> response = orderService.acceptOrder(member, orderId);
        return ApiResponse.of(response, response.getData());
    }

    @TrackOrder
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponse<OrderOwnerResponseDto>> rejectOrder(@PathVariable long orderId){
        Member member = new Member();
        ApiResponse<OrderOwnerResponseDto> response = orderService.rejectOrder(member, orderId);
        return ApiResponse.of(response, response.getData());
    }

    @TrackOrder
    @PutMapping("/{orderId}/next")
    public ResponseEntity<ApiResponse<OrderOwnerResponseDto>> proceedOrder(@PathVariable long orderId){
        Member member = new Member();
        ApiResponse<OrderOwnerResponseDto> response = orderService.proceedOrder(member, orderId);
        return ApiResponse.of(response, response.getData());
    }

}
