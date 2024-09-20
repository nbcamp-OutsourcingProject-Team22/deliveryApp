package com.sparta.deliveryapp.domain.order.controller;

import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
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

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(@PathVariable long orderId){

        orderService.acceptOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable long orderId){
        Member member = new Member(); // 추후 바꿔야함

        orderService.rejectOrder(orderId);
        return ResponseEntity.ok().build();
    }

    //@PutMapping("/{orderId}/next")

}
