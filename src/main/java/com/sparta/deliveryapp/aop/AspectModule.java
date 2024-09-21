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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
@Component
public class AspectModule {

    // 어노테이션으로 포인트컷
    @Pointcut("@annotation(com.sparta.deliveryapp.annotation.TrackOrder)")
    private void trackOrderAnnotation(){}


    @Around("trackOrderAnnotation()")
    public Object trackOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        // API 요청 시각
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        Object result = null;
        try {
            result = joinPoint.proceed();  // 메서드 실행
        } finally {
            log.info("::: API 요청 시각 : {} :::", formattedDate);

            // 메서드의 파라미터로 전달된 OrderRequestDto 객체 추출
            Object[] args = joinPoint.getArgs();
            OrderRequestDto orderRequestDto = null;

            for (Object arg : args) {
                if (arg instanceof OrderRequestDto) {
                    orderRequestDto = (OrderRequestDto) arg;
                    break;
                }
            }

            if (orderRequestDto != null) {
                log.info("::: 가게 ID : {} :::", orderRequestDto.getStoreId());
            }

            // 반환된 정보에서 출력
            OrderResponseDto response = (OrderResponseDto) result;
            if (response != null) {
                log.info("::: 주문 ID : {} :::", response.getOrderId());
            }
        }

        return result;  // 메서드 실행 결과 반환
    }

    // orderId가 입력으로 들어가고, storeId와 상태가 반환된다.
    @Around("trackOrderAnnotation() && args(orderId, ..)")
    public Object trackOrderWithId(ProceedingJoinPoint joinPoint, long orderId) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // API 요청 시각
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        Object result = null;
        try {
            result = joinPoint.proceed();  // 메서드 실행
        } finally {
            log.info("::: API 요청 시각 : {} :::", formattedDate);
            log.info("::: 주문 ID : {} :::", orderId);

            // 반환된 정보에서 출력
            OrderOwnerResponseDto response = (OrderOwnerResponseDto) result;
            if (response != null) {
                log.info("::: 가게 ID : {} :::", response.getStoreId());
                log.info("::: 상태 : {} :::", response.getProcess());
            }
        }

        return result;  // 메서드 실행 결과 반환
    }
}
