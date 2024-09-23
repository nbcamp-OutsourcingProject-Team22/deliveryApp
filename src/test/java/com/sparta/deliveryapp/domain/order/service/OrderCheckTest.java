package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.dto.OrderUserResponseDto;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.*;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderCheckTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OrderService orderService;

    private AuthMember authMember;
    private Member member;
    private Store store;
    private Menu menu;
    private Order order;

    @BeforeEach
    void setUp(){
        authMember = mock(AuthMember.class);
        ReflectionTestUtils.setField(authMember,"id",1L);
        member = mock(Member.class);
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(member, "userRole", UserRole.USER);
        store = mock(Store.class);
        ReflectionTestUtils.setField(store, "id", 1L);
        menu = mock(Menu.class);
        ReflectionTestUtils.setField(menu, "id", 1L);
        order = new Order(member, store, menu, OrderStatusEnum.REQUEST);
        ReflectionTestUtils.setField(order, "id", 1L);
    }

    @Test
    void 조회_성공() {
        given(member.getUserRole()).willReturn(UserRole.USER);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(order.getMember().getId()).willReturn(1L);
        given(member.getId()).willReturn(1L);

        ApiResponse<OrderUserResponseDto> ret = orderService.checkOrder(authMember, order.getId());

        assertThat(ret.getMessage()).isEqualTo("주문 조회에 성공하였습니다.");
        assertThat(ret.getData().getProcess()).isEqualTo(OrderStatusEnum.REQUEST.getProcess());
    }

    @Test
    void Order_유저아님(){

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(member.getUserRole()).willReturn(UserRole.OWNER);

        // when & then
        HandleUnauthorizedException exception = assertThrows(HandleUnauthorizedException.class, () -> {
            orderService.checkOrder(authMember, order.getId());
        });

        assertEquals("권한이 없습니다.",exception.getApiResponseEnum().getMessage());
    }

    @Test
    void 주문_없음() {
        given(member.getUserRole()).willReturn(UserRole.USER);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        HandleNotFound exception = assertThrows(HandleNotFound.class,()->{
            orderService.checkOrder(authMember, order.getId());
        });

        assertEquals("주문을 찾을 수 없습니다.", exception.getApiResponseEnum().getMessage());
    }

    @Test
    void 주문_사람_불일치() {
        // given: authMember와 order의 member가 다를 때
        given(memberRepository.findById(authMember.getId()))
                .willReturn(Optional.of(member));

        // member의 userRole을 지정
        given(member.getUserRole()).willReturn(UserRole.USER);
        given(member.getId()).willReturn(1L); // authMember와 같은 ID 설정

        // 다른 유저로 설정
        Member anotherMember = mock(Member.class);
        given(anotherMember.getId()).willReturn(2L);  // 다른 유저 ID 설정


        Order anotherOrder = new Order(anotherMember, store, menu, OrderStatusEnum.REQUEST);
        ReflectionTestUtils.setField(anotherOrder, "id", 1L);

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(anotherOrder));

        // when & then: 예외가 발생하는지 확인

        HandleUnauthorizedException exception = assertThrows(HandleUnauthorizedException.class,()->{
            orderService.checkOrder(authMember, order.getId());
        });

        assertEquals("권한이 없습니다.", exception.getApiResponseEnum().getMessage());

    }



}
