package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.*;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public OrderResponseDto requestOrder(Member member, OrderRequestDto orderRequestDto) {

        Store store = storeRepository.findById(orderRequestDto.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));


        Order order = new Order(member,store,menu, OrderStatusEnum.REQUEST);
        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDto(savedOrder.getId());

    }

    public String checkOrder(Member member, long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if(!Objects.equals(order.getMember().getId(), member.getId())) {
            throw new IllegalArgumentException("해당 주문에 대한 권한이 없습니다.");
        }

        return order.getStatus().getProcess();
    }
}

