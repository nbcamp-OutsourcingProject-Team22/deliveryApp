package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.*;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public OrderResponseDto requestOrder(OrderRequestDto orderRequestDto) {
        // 일단 멤머를 실험용으로
        Member member = new Member();

        Store store = storeRepository.findById(orderRequestDto.getStoreId())
                .orElseThrow(() -> new InvalidRequestStateException("가게를 찾을 수 없습니다."));

        Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                .orElseThrow(() -> new InvalidRequestStateException("메뉴를 찾을 수 없습니다"));


        Order order = new Order(member,store,menu, OrderStatusEnum.REQUEST);
        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDto(savedOrder.getId());

    }
}

