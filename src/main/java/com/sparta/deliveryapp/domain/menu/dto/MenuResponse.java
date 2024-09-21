package com.sparta.deliveryapp.domain.menu.dto;

import com.sparta.deliveryapp.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponse {
    private final Long id;
    private final String menuName;
    private final Integer menuPrice;
    private final String menuDescription;

    // 매개변수 생성자
    public MenuResponse(Menu menu){
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuDescription = menu.getMenuDescription();
    }

    // 기본생성자 (아래 -> 위로 오버로딩된것)
    public MenuResponse(Long id, String menuName, Integer menuPrice, String menuDescription) {
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
    }
}
