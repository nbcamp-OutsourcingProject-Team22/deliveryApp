package com.sparta.deliveryapp.domain.order.validator;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseOrderEnum;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import com.sparta.deliveryapp.exception.InvalidRequestException;

import java.util.Objects;

public class OrderValid {


    /**
     * 폐업 여부 확인
     * @param orderMember 주문자
     * @param signInMember 로그인 한 사람
     * @throws HandleUnauthorizedException 로그인한 사람이 주문자가 아닌 경우
     */
    public static void isOrderer(Member orderMember, Member signInMember) {
        if(!Objects.equals(orderMember.getId(), signInMember.getId())) {
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_USER);
        }
    }

    /**
     * 폐업 여부 확인
     * @param status 주문 상태
     * @param orderStatusEnum 비교 상태
     * @throws InvalidRequestException 동일하지 않으면 예외
     */
    public static void isStatusDifferent(OrderStatusEnum status, OrderStatusEnum orderStatusEnum) {
        if(!status.equals(orderStatusEnum)){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_IN_PROGRESS);
        }

    }

    /**
     * 폐업 여부 확인
     * @param status 주문 상태
     * @param orderStatusEnum 비교 상태
     * @throws InvalidRequestException 동일하면 예외
     */
    public static void isStatusSame(OrderStatusEnum status, OrderStatusEnum orderStatusEnum) {
        if(status.equals(orderStatusEnum)){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_DONE);
        }
    }
}
