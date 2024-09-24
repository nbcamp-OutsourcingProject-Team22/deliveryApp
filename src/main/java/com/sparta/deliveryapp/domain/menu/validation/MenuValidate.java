package com.sparta.deliveryapp.domain.menu.validation;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMenuEnum;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;

import java.util.Objects;

public class MenuValidate {

    // 메뉴 확인 메서드
    public static Menu validateMenu (Long storeId, Menu menu){
        if (!Objects.equals(menu.getStore().getId(), storeId)){
            throw new HandleUnauthorizedException(ApiResponseMenuEnum.NOT_OWNER);
        }
        return menu;
    }
}
