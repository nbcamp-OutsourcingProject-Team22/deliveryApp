package com.sparta.deliveryapp.domain.order.service;

import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;

import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Order;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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
    void Order_주문저장() {
        // given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
        given(orderRepository.save(any(Order.class))).willReturn(order);


        // when
        String response = orderService.requestOrder(member, orderRequestDto).getMessage();

        // then
        assertThat(response).isEqualTo("주문 저장에 성공하였습니다.");
    }


    @Test
    void Order_가게없음(){
        // given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        given(storeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        HandleNotFound exception = assertThrows(HandleNotFound.class, () -> {
            orderService.requestOrder(member, orderRequestDto);
        });

        assertEquals("가게를 찾을 수 없습니다.",exception.getApiResponseEnum().getMessage());
    }

    @Test
    void Order_메뉴없음(){
        // given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        Store store = mock(Store.class);
        ReflectionTestUtils.setField(store, "id", 1L);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        HandleNotFound exception = assertThrows(HandleNotFound.class, () -> {
            orderService.requestOrder(member, orderRequestDto);
        });

        assertEquals("메뉴를 찾을 수 없습니다.",exception.getApiResponseEnum().getMessage());
    }
}
