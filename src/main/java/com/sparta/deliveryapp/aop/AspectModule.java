package com.sparta.deliveryapp.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
public class AspectModule {

    // 포인트 컷 어노테이션 ver
    @Pointcut("@annotation(com.sparta.deliveryapp.annotation.TrackAdmin)")
    private void trackAdminAnnotation(){}

    @After("trackAdminAnnotation()")
    public void afterTrackAdmin() {


    }

}
