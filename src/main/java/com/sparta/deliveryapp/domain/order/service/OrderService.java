package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseOrderEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderUserResponseDto;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.*;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public ApiResponse<OrderResponseDto> requestOrder(Member member, OrderRequestDto orderRequestDto) {

        Store store = storeRepository.findById(orderRequestDto.getStoreId())
                .orElseThrow(() ->  new HandleNotFound(ApiResponseOrderEnum.STORE_NOT_FOUND));

        Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.MENU_NOT_FOUND));


        Order order = new Order(member,store,menu, OrderStatusEnum.REQUEST);
        Order savedOrder = orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto(savedOrder.getId());
        return new ApiResponse<>(ApiResponseOrderEnum.ORDER_SAVE_SUCCESS,orderResponseDto);

    }

    public ApiResponse<OrderUserResponseDto> checkOrder(Member member, long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.ORDER_NOT_FOUND));

        if(!Objects.equals(order.getMember().getId(), member.getId())) {
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_OWNER);
        }

        OrderUserResponseDto orderUserResponseDto = new OrderUserResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return new ApiResponse<>(ApiResponseOrderEnum.ORDER_CHECK_SUCCESS,orderUserResponseDto);
    }

    public ApiResponse<OrderOwnerResponseDto> acceptOrder(Member member, long orderId) {
        Order order =checkOrderStatus(orderId);
        //주인 맞는지 체크 해야함
//      checkOwnerOfStore(order, member);
        order.changeStatus(OrderStatusEnum.ACCEPTED);

        OrderOwnerResponseDto orderOwnerResponseDto = new OrderOwnerResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return new ApiResponse<>(ApiResponseOrderEnum.ORDER_ACCEPT_SUCCESS,orderOwnerResponseDto);
    }

    public ApiResponse<OrderOwnerResponseDto> rejectOrder(Member member,long orderId) {
        Order order =checkOrderStatus(orderId);
        //주인 맞는지 체크 해야함
//        checkOwnerOfStore(order, member);
        order.changeStatus(OrderStatusEnum.REJECTED);
        OrderOwnerResponseDto orderOwnerResponseDto = new OrderOwnerResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return new ApiResponse<>(ApiResponseOrderEnum.ORDER_REJECT_SUCCESS,orderOwnerResponseDto);
    }

    public ApiResponse<OrderOwnerResponseDto> proceedOrder(Member member, long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.ORDER_NOT_FOUND));

//        checkOwnerOfStore(order, member);

        if(order.getStatus()==OrderStatusEnum.DELIVERED){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_COMPLETE);
        }
        if(order.getStatus()==OrderStatusEnum.REJECTED){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_REJECT);
        }

        order.changeStatus(OrderStatusEnum.values()[order.getStatus().getNum()]);
        OrderOwnerResponseDto orderOwnerResponseDto = new OrderOwnerResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return new ApiResponse<>(ApiResponseOrderEnum.ORDER_PROCEED_SUCCESS,orderOwnerResponseDto);
    }

    private Order checkOrderStatus(long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.ORDER_NOT_FOUND));

        if(!order.getStatus().equals(OrderStatusEnum.REQUEST)){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_IN_PROGRESS);
        }

        return order;
    }


//    private void checkOwnerOfStore(Order order, Member member) {
//        Store store = order.getStore();
//
//        // Null 검사 추가
//        if (store == null || store.getMember() == null || member == null) {
//            throw new IllegalArgumentException("가게 또는 멤버 정보가 누락되었습니다.");
//        }
//
//        if (!store.getMember().equals(member)) {
//            throw new IllegalArgumentException("해당 가게의 주인이 아닙니다.");
//        }
//    }




}

