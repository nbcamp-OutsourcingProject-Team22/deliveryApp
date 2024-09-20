package com.sparta.deliveryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;


@SpringBootApplication
@EnableJpaAuditing
//@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class DeliveryAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryAppApplication.class, args);
    }

}
