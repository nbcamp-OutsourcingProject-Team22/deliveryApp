package com.sparta.deliveryapp.domain.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MenuRequest {

    @NotNull(message = "메뉴이름은 공백일 수 없습니다.")
    private String menuName;

    @NotNull(message = "메뉴가격은 공백일 수 없습니다.")
    private Integer menuPrice;

    @NotNull(message = "메뉴설명은 공백일 수 없습니다.")
    private String menuDescription;
}
