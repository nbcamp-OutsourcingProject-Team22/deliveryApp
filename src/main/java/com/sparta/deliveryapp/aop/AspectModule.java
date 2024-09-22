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


    // 요청시각, 주문id, 가게 id 필수
    // 1. requestOrder  -> 입력으로 storeId, menuId 반환으로 주문 id
    // 2. 나머지          -> 입력으로 orderId, 반환으로 storeId

    @Around("trackOrderAnnotation()")
    public Object trackOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        // API 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        // 위 주석에 따라 requestOrder는 동작이 다르기 때문에 메서드 이름으로 구분
        String methodName = joinPoint.getSignature().getName();

        // 메서드의 파라미터 가져오기
        Object[] args = joinPoint.getArgs();

        Long orderId = null;
        Long storeId = null;

        if(methodName.equals("requestOrder")){
            OrderRequestDto OrderRequestDto = (OrderRequestDto) args[0];
            storeId = OrderRequestDto.getStoreId();

            log.info("::: API 요청 시각 : {} :::", requestTime);
            log.info("::: 가게 Id : {} :::", storeId);
        }
        else{
            orderId = (Long) args[0];
            log.info("::: API 요청 시각 : {} :::", requestTime);
            log.info("::: 주문 Id : {} :::", orderId);
        }

        Object result = null;

        try{
            result = joinPoint.proceed();
        }catch(Exception e){
            log.info("::: 예상 못한 오류 발생 : {} :::", e.getMessage());
            throw e;

        }finally {
            if(methodName.equals("requestOrder")){
                OrderResponseDto orderResponse = (OrderResponseDto) result;
                orderId = orderResponse.getOrderId();
                log.info("::: 주문 Id : {} :::", orderId);
            }
            else{
                OrderOwnerResponseDto orderOwnerResponseDto = (OrderOwnerResponseDto) result;
                storeId = orderOwnerResponseDto.getStoreId();
                log.info("::: 가게 Id : {} :::", storeId);
            }

        }

        return result;
    }


}
