package com.sparta.deliveryapp.aop;

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

    @Pointcut("@annotation(com.sparta.deliveryapp.annotation.TrackFirstOrder)")
    private void trackFirstOrderAnnotation(){}


    @Around("trackFirstOrderAnnotation()")
    public Object afterTrackFirstOrder(ProceedingJoinPoint joinPoint)throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // API 요청 시각
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);


        try {
            //after retruning
            Object result = joinPoint.proceed();
            return result;
            //가게 id
            //주문 id

        }
        finally {
            log.info("::: API 요청 시각 : {} :::", formattedDate);

        }

    }

    //요청 시각, 가게 id, 주문 id
    @Around("trackOrderAnnotation()")
    public Object afterTrackOrder(ProceedingJoinPoint joinPoint)throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // API 요청 시각
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);


        try {
            //after retruning
            Object result = joinPoint.proceed();
            return result;
            //가게 id
            //주문 id

        }
        finally {
            log.info("::: API 요청 시각 : {} :::", formattedDate);

        }

    }

}
