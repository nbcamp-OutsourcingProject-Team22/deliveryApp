package com.sparta.deliveryapp.domain.Order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryapp.domain.order.controller.OrderUserController;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.service.OrderService;
import com.sparta.deliveryapp.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderUserController.class)
public class OrderUserControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderUserController orderUserController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(orderUserController).build();
    }

    @Test
    void 주문_생성()throws Exception{
        OrderResponseDto orderResponseDto = new OrderResponseDto(1L);
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L);

        given(orderService.requestOrder(any(Member.class), any(OrderRequestDto.class))).willReturn(orderResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/user/orders/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequestDto))
        );

        resultActions.andExpect(status().isOk());

    }




}
