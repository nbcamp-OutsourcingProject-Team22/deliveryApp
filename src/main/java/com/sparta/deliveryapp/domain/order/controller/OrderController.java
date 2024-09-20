package com.sparta.deliveryapp.domain.order.controller;

import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/request")
    public ResponseEntity<OrderResponseDto> requestOrder(@RequestBody OrderRequestDto orderRequestDto){

        OrderResponseDto orderResponse = orderService.requestOrder(orderRequestDto);
        return ResponseEntity.ok(orderResponse);

    }

}
