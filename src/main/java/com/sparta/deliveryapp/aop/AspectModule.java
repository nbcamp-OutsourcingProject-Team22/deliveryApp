package com.sparta.deliveryapp.aop;

import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

        // 첫 번째 인자는 AuthMember, 두 번째 인자는 OrderRequestDto
        if (methodName.equals("requestOrder")) {
            OrderRequestDto orderRequestDto = (OrderRequestDto) args[1];  // args[1]로 수정
            storeId = orderRequestDto.getStoreId();

            log.info("::: API 요청 시각 : {} :::", requestTime);
            log.info("::: 가게 Id : {} :::", storeId);
        } else {
            orderId = (Long) args[0];
            log.info("::: API 요청 시각 : {} :::", requestTime);
            log.info("::: 주문 Id : {} :::", orderId);
        }

        Object result = null;

        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.info("::: 예상 못한 오류 발생 : {} :::", e.getMessage());
            throw e;
        } finally {
            if (methodName.equals("requestOrder")) {
                OrderResponseDto orderResponse = (OrderResponseDto)result;
                orderId = orderResponse.getOrderId();
                log.info("::: 주문 Id : {} :::", orderId);
            } else {
                OrderOwnerResponseDto orderOwnerResponseDto = (OrderOwnerResponseDto) result;
                storeId = orderOwnerResponseDto.getStoreId();
                log.info("::: 가게 Id : {} :::", storeId);
            }
        }

        return result;
    }



}
