package com.sparta.deliveryapp.domain.Order;

import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.*;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class OrderCheckTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Member member;
    private Store store;
    private Menu menu;
    private Order order;

    @BeforeEach
    void setUp(){
        member = mock(Member.class);
        ReflectionTestUtils.setField(member, "id", 1L);
        store = mock(Store.class);
        ReflectionTestUtils.setField(store, "id", 1L);
        menu = mock(Menu.class);
        ReflectionTestUtils.setField(menu, "id", 1L);
        order = new Order(member, store, menu, OrderStatusEnum.REQUEST);
        ReflectionTestUtils.setField(order, "id", 1L);
    }

    @Test
    void 조회_성공() {


        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(order.getMember().getId()).willReturn(1L);
        given(member.getId()).willReturn(1L);

        String ret = orderService.checkOrder(member,order.getId());

        assertThat(ret).isEqualTo(OrderStatusEnum.REQUEST.getProcess());
    }


    @Test
    void 주문_없음() {
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,()->{
            orderService.checkOrder(member,order.getId());
        });

        assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 주문_사람_불일치() {

        // order에 다른 member를 설정
        Member otherMember = new Member();
        ReflectionTestUtils.setField(otherMember, "id", 2L); // 다른 멤버의 ID 설정

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // order의 member와 테스트의 member가 다름
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.checkOrder(otherMember, order.getId());
        });

        assertEquals("해당 주문에 대한 권한이 없습니다.", exception.getMessage());

    }
}
