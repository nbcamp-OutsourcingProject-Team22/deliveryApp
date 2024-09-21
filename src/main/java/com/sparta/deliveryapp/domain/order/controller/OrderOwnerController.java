package com.sparta.deliveryapp.domain.order.controller;

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
        Member member = new Member();
        orderService.acceptOrder(member,orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable long orderId){
        Member member = new Member();
        orderService.rejectOrder(member,orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/next")
    public ResponseEntity<String> proceedOrder(@PathVariable long orderId){
        Member member = new Member();
        String proceedResponse = orderService.proceedOrder(member, orderId);
        return ResponseEntity.ok(proceedResponse);
    }

}
