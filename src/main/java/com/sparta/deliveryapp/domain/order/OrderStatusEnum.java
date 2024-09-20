package com.sparta.deliveryapp.domain.order;

import lombok.Getter;

import java.util.InvalidPropertiesFormatException;

@Getter
public enum OrderStatusEnum {

    REQUEST(1, "Request"),          // 주문 요청
    ACCEPTED(2, "Accepted"),        // 주문 수락
    PREPARING(3, "Preparing"),      // 준비 중
    IN_DELIVERY(4, "In Delivery"),  // 배달 중
    DELIVERED(5, "Delivered"),      // 배달 완료
    REJECTED(10,"rejected") ;       // 배달 거부

    private final int order;
    private final String status;

    OrderStatusEnum(int order, String status) {
        this.order = order;
        this.status = status;
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
