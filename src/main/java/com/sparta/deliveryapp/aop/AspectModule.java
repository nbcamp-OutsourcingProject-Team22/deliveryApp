package com.sparta.deliveryapp.aop;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderUserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AspectModule {

    // 어노테이션으로 포인트컷
    @Pointcut("@annotation(com.sparta.deliveryapp.annotation.TrackOrder)")
    private void trackOrderAnnotation(){}

    // 요청시각, 주문id, 가게 id 필수
    // 1. requestOrder  -> 입력으로 storeId, menuId 반환으로 주문 id
    // 2. 나머지          -> 입력으로 orderId, 반환으로 storeId

    @Around("trackOrderAnnotation()")
    public Object trackOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        // API 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        Long orderId = null;
        Long storeId = null;
        String statusProcess = null;

        try {
            Object result = joinPoint.proceed();

            if (methodName.equals("requestOrder")) {
                ResponseEntity<ApiResponse<OrderResponseDto>> responseEntity = (ResponseEntity<ApiResponse<OrderResponseDto>>) result;
                ApiResponse<OrderResponseDto> orderResponseDto = responseEntity.getBody();
                OrderRequestDto orderRequestDto = (OrderRequestDto) args[1]; // 첫 번째 인자는 AuthMember, 두 번째 인자는 OrderRequestDto
                storeId = orderRequestDto.getStoreId();
                orderId = orderResponseDto.getData().getOrderId();

            } else if(methodName.equals("checkOrder")) {
                ResponseEntity<ApiResponse<OrderUserResponseDto>> responseEntity = (ResponseEntity<ApiResponse<OrderUserResponseDto>>) result;
                ApiResponse<OrderUserResponseDto> orderUserResponseDto = responseEntity.getBody();
                orderId = (Long) args[1];
                storeId = orderUserResponseDto.getData().getStoreId();
                statusProcess = orderUserResponseDto.getData().getProcess();
            }
            else {
                ResponseEntity<ApiResponse<OrderOwnerResponseDto>> responseEntity = (ResponseEntity<ApiResponse<OrderOwnerResponseDto>>) result;
                ApiResponse<OrderOwnerResponseDto> orderOwnerResponseDto = responseEntity.getBody();
                orderId = (Long) args[1];
                storeId = orderOwnerResponseDto.getData().getStoreId();
                statusProcess = orderOwnerResponseDto.getData().getProcess();

            }
            log.info("::: 주문 Id : {} :::", orderId);
            log.info("::: 가게 Id : {} :::",storeId);
            log.info("::: API 요청 시각 : {} :::", requestTime);

            if(statusProcess!=null){
                log.info("::: 주문 상태 : {} :::",statusProcess);
            }

            return result;

        }catch(Exception e){
            log.info("{}", e.getMessage());
            throw e;
        }
    }
}
