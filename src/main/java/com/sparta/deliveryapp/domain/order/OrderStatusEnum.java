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


    private int order;
    private String process;

    OrderStatusEnum(int order, String process) {
        this.order = order;
        this.process = process;
    }

    public OrderStatusEnum next() throws InvalidPropertiesFormatException {
        if(this.order==5){
            throw new InvalidPropertiesFormatException("이미 완료된 주문입니다.");
        }
        if(this.order==10){
            throw new InvalidPropertiesFormatException("이미 거부된 주문입니다.");
        }

        return OrderStatusEnum.values()[this.order];
    }


}
