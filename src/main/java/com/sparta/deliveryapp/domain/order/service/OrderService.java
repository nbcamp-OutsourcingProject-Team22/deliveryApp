package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMenuEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseOrderEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
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
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;


    // 주문 생성 (유저)
    @Transactional
    public ApiResponse<OrderResponseDto> requestOrder(AuthMember authMember, OrderRequestDto orderRequestDto) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.MEMBER_NOT_FOUND));

        // UserRole에 따라 필터에서 걸러지면 이건 지워주세용
        if(member.getUserRole().equals(UserRole.OWNER)){
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_USER);
        }

        Store store = storeRepository.findById(orderRequestDto.getStoreId())
                .orElseThrow(() ->  new HandleNotFound(ApiResponseStoreEnum.STORE_NOT_FOUND));

        Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                .orElseThrow(() -> new HandleNotFound(ApiResponseMenuEnum.MENU_NOT_FOUND));


        Order order = new Order(
                member,
                store,
                menu,
                OrderStatusEnum.REQUEST
        );
        Order savedOrder = orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto(savedOrder.getId());
        return ApiResponse.ofApiResponseEnum(ApiResponseOrderEnum.ORDER_SAVE_SUCCESS,orderResponseDto);

    }

    // 주문 상태 조회 (유저)
    public ApiResponse<OrderUserResponseDto> checkOrder(AuthMember authMember, long orderId) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(()-> new HandleNotFound(ApiResponseOrderEnum.MEMBER_NOT_FOUND));

        // UserRole에 따라 필터에서 걸러지면 이건 지워주세용
        if(!member.getUserRole().equals(UserRole.USER)){
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_USER);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.ORDER_NOT_FOUND));

        if(!Objects.equals(order.getMember().getId(), member.getId())) {
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_USER);
        }

        OrderUserResponseDto orderUserResponseDto = new OrderUserResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return ApiResponse.ofApiResponseEnum(ApiResponseOrderEnum.ORDER_CHECK_SUCCESS,orderUserResponseDto);
    }

    // 주문 수락 (주인)
    @Transactional
    public ApiResponse<OrderOwnerResponseDto> acceptOrder(AuthMember authMember, long orderId) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(()-> new HandleNotFound(ApiResponseOrderEnum.MEMBER_NOT_FOUND));

        // UserRole에 따라 필터에서 걸러지면 이건 지워주세용
        if(!member.getUserRole().equals(UserRole.OWNER)){
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_OWNER);
        }

        Order order =checkOrderStatus(orderId);

        //주인 맞는지 체크 해야함
        checkOwnerOfStore(order, member);
        order.changeStatus(OrderStatusEnum.ACCEPTED);
        orderRepository.save(order);


        OrderOwnerResponseDto orderOwnerResponseDto = new OrderOwnerResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return ApiResponse.ofApiResponseEnum(ApiResponseOrderEnum.ORDER_ACCEPT_SUCCESS,orderOwnerResponseDto);
    }

    // 주문 거절 (주인)
    @Transactional
    public ApiResponse<OrderOwnerResponseDto> rejectOrder(AuthMember authMember,long orderId) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(()-> new HandleNotFound(ApiResponseOrderEnum.MEMBER_NOT_FOUND));

        // UserRole에 따라 필터에서 걸러지면 이건 지워주세용
        if(!member.getUserRole().equals(UserRole.OWNER)){
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_OWNER);
        }

        Order order =checkOrderStatus(orderId);
        //주인 맞는지 체크 해야함
        checkOwnerOfStore(order, member);
        order.changeStatus(OrderStatusEnum.REJECTED);
        orderRepository.save(order);

        OrderOwnerResponseDto orderOwnerResponseDto = new OrderOwnerResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return ApiResponse.ofApiResponseEnum(ApiResponseOrderEnum.ORDER_REJECT_SUCCESS,orderOwnerResponseDto);
    }

    // 주문 진행 (주인)
    @Transactional
    public ApiResponse<OrderOwnerResponseDto> proceedOrder(AuthMember authMember, long orderId) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(()-> new HandleNotFound(ApiResponseOrderEnum.MEMBER_NOT_FOUND));

        // UserRole에 따라 필터에서 걸러지면 이건 지워주세용
        if(!member.getUserRole().equals(UserRole.OWNER)){
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_OWNER);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.ORDER_NOT_FOUND));

        checkOwnerOfStore(order, member);

        if(order.getStatus()==OrderStatusEnum.DELIVERED){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_COMPLETE);
        }
        if(order.getStatus()==OrderStatusEnum.REJECTED){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_REJECT);
        }

        order.changeStatus(OrderStatusEnum.values()[order.getStatus().getNum()+1]);
        orderRepository.save(order);
        OrderOwnerResponseDto orderOwnerResponseDto = new OrderOwnerResponseDto(order.getStore().getId(), order.getStatus().getProcess());
        return ApiResponse.ofApiResponseEnum(ApiResponseOrderEnum.ORDER_PROCEED_SUCCESS,orderOwnerResponseDto);
    }

    private Order checkOrderStatus(long orderId){

        // 주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseOrderEnum.ORDER_NOT_FOUND));


        // 주문 상태가 최초가 아니면 예외
        if(!order.getStatus().getProcess().equals(OrderStatusEnum.REQUEST.getProcess())){
            throw new InvalidRequestException(ApiResponseOrderEnum.ORDER_IN_PROGRESS);
        }

        return order;
    }


    private void checkOwnerOfStore(Order order, Member member) {
        Store store = order.getStore();

        if (!member.equals(store.getMember())) {
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_OWNER);
        }
    }
}

