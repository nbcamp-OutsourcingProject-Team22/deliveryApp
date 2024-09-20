package com.sparta.deliveryapp.domain.order;

import lombok.Getter;

import java.util.InvalidPropertiesFormatException;

@Getter
public enum OrderStatusEnum {

    REQUEST(1, "REQUEST"),          // 주문 요청
    ACCEPTED(2, "ACCEPTED"),        // 주문 수락
    PREPARING(3, "PREPARING"),      // 준비 중
    IN_DELIVERY(4, "IN DELIVERY"),  // 배달 중
    DELIVERED(5, "DELIVERED"),      // 배달 완료
    REJECTED(10,"REJECTED") ;       // 배달 거부


    private int num;
    private String process;

    OrderStatusEnum(int num, String process) {
        this.num = num;
        this.process = process;
    }




}
