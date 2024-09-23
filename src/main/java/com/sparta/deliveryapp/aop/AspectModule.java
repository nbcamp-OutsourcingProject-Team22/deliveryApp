package com.sparta.deliveryapp.aop;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.order.dto.OrderOwnerResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderRequestDto;
import com.sparta.deliveryapp.domain.order.dto.OrderResponseDto;
import com.sparta.deliveryapp.domain.order.dto.OrderUserResponseDto;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;
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

        if (methodName.equals("requestOrder")) {
            OrderRequestDto orderRequestDto = (OrderRequestDto) args[1]; // 첫 번째 인자는 AuthMember, 두 번째 인자는 OrderRequestDto
            storeId = orderRequestDto.getStoreId();

            log.info("::: API 요청 시각 : {} :::", requestTime);
            log.info("::: 가게 Id : {} :::", storeId);
        } else {
            orderId = (Long) args[1];
            log.info("::: API 요청 시각 : {} :::", requestTime);
            log.info("::: 주문 Id : {} :::", orderId);
        }

        Object result = null;

        try {
            result = joinPoint.proceed();
//        } catch (Exception e) {
//            log.info("::: 예상 못한 오류 발생 : {} :::", e.getMessage());
//            throw e;
        }catch(HandleNotFound | InvalidRequestException | HandleUnauthorizedException e){
            log.info("{}", e.getMessage());
            throw e;
        }
        finally {
            if (methodName.equals("requestOrder")) {
                ResponseEntity<ApiResponse<OrderResponseDto>> responseEntity = (ResponseEntity<ApiResponse<OrderResponseDto>>) result;
                ApiResponse<OrderResponseDto> orderResponseDto = responseEntity.getBody();
                log.info("::: 주문 Id : {} :::", orderResponseDto.getData().getOrderId());
            } else if(methodName.equals("checkOrder")) {

                ResponseEntity<ApiResponse<OrderUserResponseDto>> responseEntity = (ResponseEntity<ApiResponse<OrderUserResponseDto>>) result;
                ApiResponse<OrderUserResponseDto> orderUserResponseDto = responseEntity.getBody();
                log.info("::: 가게 Id : {} :::",orderUserResponseDto.getData().getStoreId() );
                log.info("::: 주문 상태 : {} :::",orderUserResponseDto.getData().getProcess());
            }
            else {
                ResponseEntity<ApiResponse<OrderOwnerResponseDto>> responseEntity = (ResponseEntity<ApiResponse<OrderOwnerResponseDto>>) result;
                ApiResponse<OrderOwnerResponseDto> orderOwnerResponseDto = responseEntity.getBody();
                log.info("::: 가게 Id : {} :::",orderOwnerResponseDto.getData().getStoreId());
                log.info("::: 주문 상태 : {} :::",orderOwnerResponseDto.getData().getProcess());
            }
        }

        return result;
    }



}
