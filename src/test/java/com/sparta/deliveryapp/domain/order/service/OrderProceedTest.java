package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Order;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.InvalidRequestException;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;



@ExtendWith(MockitoExtension.class)
public class OrderProceedTest {

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

    @BeforeEach
    void setUp(){
        member = mock(Member.class);
        ReflectionTestUtils.setField(member, "id", 1L);
        store = mock(Store.class);
        ReflectionTestUtils.setField(store, "id", 1L);
        menu = mock(Menu.class);
        ReflectionTestUtils.setField(menu, "id", 1L);

    }

    @Test
    void 주문_진행_성공(){
        Order order = new Order(member, store, menu, OrderStatusEnum.PREPARING);
        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        ApiResponse<OrderOwnerResponseDto> ret = orderService.proceedOrder(member, order.getId());

        assertThat(ret.getMessage()).isEqualTo("주문 진행에 성공하였습니다.");
        assertThat(ret.getData().getProcess()).isEqualTo("IN DELIVERY");

    }
    @Test
    void 이미_완료된_주문(){
        Order order = new Order(member, store, menu, OrderStatusEnum.DELIVERED);
        ReflectionTestUtils.setField(order, "id", 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->{
            orderService.proceedOrder(member, order.getId());
        });

        assertEquals("완료된 주문입니다.",exception.getApiResponseEnum().getMessage());

    }
    @Test
    void 이미_거부된_주문_진행(){
        Order order = new Order(member, store, menu, OrderStatusEnum.REJECTED);
        ReflectionTestUtils.setField(order, "id", 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->{
            orderService.proceedOrder(member, order.getId());
        });

        assertEquals("거부된 주문입니다.",exception.getApiResponseEnum().getMessage());
    }
}