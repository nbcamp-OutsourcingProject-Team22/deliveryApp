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

    public void acceptOrder(Member member,long orderId) {
        Order order =checkOrderStatus(orderId);
        //주인 맞는지 체크 해야함
//      checkOwnerOfStore(order, member);
        order.changeStatus(OrderStatusEnum.ACCEPTED);
    }

    public void rejectOrder(Member member,long orderId) {
        Order order =checkOrderStatus(orderId);
        //주인 맞는지 체크 해야함
//        checkOwnerOfStore(order, member);
        order.changeStatus(OrderStatusEnum.REJECTED);
    }

    public String proceedOrder(Member member, long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

//        checkOwnerOfStore(order, member);

        if(order.getStatus()==OrderStatusEnum.DELIVERED){
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
        if(order.getStatus()==OrderStatusEnum.REJECTED){
            throw new IllegalArgumentException("이미 거부된 주문입니다.");
        }

        order.changeStatus(OrderStatusEnum.values()[order.getStatus().getNum()]);

        return order.getStatus().getProcess();
    }

    private Order checkOrderStatus(long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        if(!order.getStatus().equals(OrderStatusEnum.REQUEST)){
            throw new IllegalArgumentException("이미 진행된 주문입니다.");
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

