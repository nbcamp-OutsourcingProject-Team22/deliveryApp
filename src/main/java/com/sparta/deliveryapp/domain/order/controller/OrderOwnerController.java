package com.sparta.deliveryapp.domain.order.controller;

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

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<OrderOwnerResponseDto> acceptOrder(@PathVariable long orderId){
        Member member = new Member();
        return ResponseEntity.ok(orderService.acceptOrder(member, orderId));
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<OrderOwnerResponseDto> rejectOrder(@PathVariable long orderId){
        Member member = new Member();
        return ResponseEntity.ok(orderService.rejectOrder(member, orderId));
    }

    @PutMapping("/{orderId}/next")
    public ResponseEntity<OrderOwnerResponseDto> proceedOrder(@PathVariable long orderId){
        Member member = new Member();
        return ResponseEntity.ok(orderService.proceedOrder(member, orderId));
    }

}
