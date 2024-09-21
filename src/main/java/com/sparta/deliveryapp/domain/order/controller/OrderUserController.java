package com.sparta.deliveryapp.domain.order.controller;

import com.sparta.deliveryapp.annotation.TrackOrder;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import com.sparta.deliveryapp.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class OrderUserController {

    private final OrderService orderService;

    // 추후 사용자 정보 받아야함
    @TrackOrder
    @PostMapping("/request")
    public ResponseEntity<OrderResponseDto> requestOrder(@RequestBody OrderRequestDto orderRequestDto){

        Member member = new Member(); // 추후 바꿔야함

        OrderResponseDto orderResponse = orderService.requestOrder(member, orderRequestDto);
        return ResponseEntity.ok(orderResponse);
    }

    // 추후 사용자 정보 받아야함
    @TrackOrder
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderOwnerResponseDto> checkOrder(@PathVariable long orderId){

        Member member = new Member(); // 추후 바꿔야함
        return ResponseEntity.ok(orderService.checkOrder(member,orderId));
    }


}
