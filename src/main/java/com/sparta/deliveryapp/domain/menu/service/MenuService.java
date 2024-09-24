package com.sparta.deliveryapp.domain.menu.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.dto.MenuResponse;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;

public interface MenuService {
    ApiResponse<Void> createMenu (AuthMember authMember, Long storeId, MenuRequest menuRequest);
    ApiResponse<MenuResponse> updateMenu(AuthMember authMember, Long storeId, Long menuId, MenuRequest menuRequest);
    ApiResponse<Void> deleteMenu(AuthMember authMember, Long storeId, Long menuId);
    Member validateMember (AuthMember authMember);
    Store validateStore (Long storeId, Member member);
    Menu validateMenu (Long menuId, Long storeId);
}