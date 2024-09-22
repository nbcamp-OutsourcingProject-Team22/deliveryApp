package com.sparta.deliveryapp.domain.order.service;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Order;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
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
    void 요청_수락(){
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.acceptOrder(member,1L);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatusEnum.ACCEPTED);
    }
    @Test
    void 요청_거절(){
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.rejectOrder(member,1L);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatusEnum.REJECTED);
    }
    @Test
    void 이미_진행된_주문() {
        // given
        order.changeStatus(OrderStatusEnum.ACCEPTED);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->{
            orderService.rejectOrder(member,1L);
        });

        // then
        assertEquals("진행중인 주문입니다.",exception.getApiResponseEnum().getMessage());
    }
//    @Test
//    void 주문_없음() {
//        // given
//        given(orderRepository.findById(1L)).willReturn(Optional.empty());
//
//        // when & then
//        assertThatThrownBy(() -> orderService.acceptOrder(member,1L))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("주문을 찾을 수 없습니다.");
//    }
}
