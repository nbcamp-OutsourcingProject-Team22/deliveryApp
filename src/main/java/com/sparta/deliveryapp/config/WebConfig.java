package com.sparta.deliveryapp.config;

import com.sparta.deliveryapp.aop.AspectModule;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {



    @Bean
    public AspectModule getAspectAop(){
        return new AspectModule();
    }
}
