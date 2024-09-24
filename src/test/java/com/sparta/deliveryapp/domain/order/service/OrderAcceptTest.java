package com.sparta.deliveryapp.domain.order.service;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Order;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class OrderAcceptTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

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
        ReflectionTestUtils.setField(member, "userRole", UserRole.OWNER);
        store = mock(Store.class);
        ReflectionTestUtils.setField(store, "id", 1L);
        menu = mock(Menu.class);
        ReflectionTestUtils.setField(menu, "id", 1L);
        order = new Order(member, store, menu, OrderStatusEnum.REQUEST);
        ReflectionTestUtils.setField(order, "id", 1L);
        given(authMember.getRole()).willReturn(UserRole.OWNER);
    }

    @Test
    void 요청_수락(){
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(authMember.getRole()).willReturn(UserRole.OWNER);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        // when
        orderServiceImpl.acceptOrder(authMember,1L);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatusEnum.ACCEPTED);
    }
    @Test
    void 요청_거절(){
        // given
        given(authMember.getRole()).willReturn(UserRole.OWNER);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderServiceImpl.rejectOrder(authMember,1L);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatusEnum.REJECTED);
    }
    @Test
    void 수락_이미_진행된_주문() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(authMember.getRole()).willReturn(UserRole.OWNER);

        ReflectionTestUtils.setField(order, "status", OrderStatusEnum.PREPARING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->{
            orderServiceImpl.acceptOrder(authMember,1L);
        });

        // then
        assertEquals("진행중인 주문입니다.",exception.getApiResponseEnum().getMessage());
    }
    @Test
    void 거절_이미_진행된_주문() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(authMember.getRole()).willReturn(UserRole.OWNER);

        ReflectionTestUtils.setField(order, "status", OrderStatusEnum.PREPARING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->{
            orderServiceImpl.rejectOrder(authMember,1L);
        });

        // then
        assertEquals("진행중인 주문입니다.",exception.getApiResponseEnum().getMessage());
    }
    @Test
    void 수락_주인_권한_아님(){

        given(authMember.getRole()).willReturn(UserRole.USER);

        // when & then
        HandleUnauthorizedException exception = assertThrows(HandleUnauthorizedException.class, () -> {
            orderServiceImpl.acceptOrder(authMember,1L);
        });

        assertEquals("권한이 없습니다.",exception.getApiResponseEnum().getMessage());
    }
    @Test
    void 거절_주인_권한_아님(){

        given(authMember.getRole()).willReturn(UserRole.USER);

        // when & then
        HandleUnauthorizedException exception = assertThrows(HandleUnauthorizedException.class, () -> {
            orderServiceImpl.rejectOrder(authMember,1L);
        });

        assertEquals("권한이 없습니다.",exception.getApiResponseEnum().getMessage());
    }
//    @Test
//    void 가게_주인_아님(){
//
//
//        Member newMember = mock(Member.class);
//        ReflectionTestUtils.setField(newMember, "id", 2L);
//
//        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
//        given(authMember.getRole()).willReturn(UserRole.OWNER);
//        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
//        given(store.getMember()).willReturn(newMember);
//
//
//        // when
//        HandleUnauthorizedException exception = assertThrows(HandleUnauthorizedException.class,
//                ()-> orderServiceImpl.acceptOrder(authMember,1L));
//
//
//        // then
//        assertEquals("권한이 없습니다.",exception.getApiResponseEnum().getMessage());
//    }
}
