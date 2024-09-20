package com.sparta.deliveryapp.domain.Order;

import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Order;
import com.sparta.deliveryapp.entity.Store;
import com.sun.jdi.request.InvalidRequestStateException;
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
import static org.mockito.BDDMockito.given;

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

    @Test
    void Order_주문생성() {
        // given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", 1L);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        // Order 객체 저장에 대한 스텁 추가
        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order, "id", 1L); // ID 설정
            return order; // 저장된 주문 객체 반환
        });

        // when
        OrderResponseDto response = orderService.requestOrder(orderRequestDto);

        // then
        assertThat(response.getOrderId()).isEqualTo(1L);
    }

    @Test
    void Order_가게없음(){
        // given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        given(storeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        InvalidRequestStateException exception = assertThrows(InvalidRequestStateException.class, () -> {
            orderService.requestOrder(orderRequestDto);
        });

        assertEquals("가게를 찾을 수 없습니다.",exception.getMessage());
    }

    @Test
    void Order_메뉴없음(){
        // given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", 1L);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        InvalidRequestStateException exception = assertThrows(InvalidRequestStateException.class, () -> {
            orderService.requestOrder(orderRequestDto);
        });

        assertEquals("메뉴를 찾을 수 없습니다.",exception.getMessage());
    }
}
