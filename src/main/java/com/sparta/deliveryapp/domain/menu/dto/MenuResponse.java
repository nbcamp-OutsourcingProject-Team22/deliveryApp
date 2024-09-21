package com.sparta.deliveryapp.domain.menu.dto;

import lombok.Getter;

@Getter
public class MenuResponse {
    private final Long id;
    private final String menuName;
    private final Integer menuPrice;
    private final String menuDescription;

    public MenuResponse(Long id, String menuName, Integer menuPrice, String menuDescription) {
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
    }
}
