package com.sparta.deliveryapp.domain.menu.controller;

import com.sparta.deliveryapp.annotation.Auth;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.dto.MenuResponse;
import com.sparta.deliveryapp.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("stores/{storeId}/menus")
public class MenuController {
    private final MenuService menuService;

    // 메뉴 생성 (controller 에서는 보내주기만 하기
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMenu (
            @Auth AuthMember authMember,
            @PathVariable Long storeId,
            @RequestBody @Valid MenuRequest menuRequest)
    {
        ApiResponse<Void> result = menuService.createMenu(authMember, storeId, menuRequest);
        return ApiResponse.of(result);
    }

    // 매뉴 수정
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu (
            @Auth AuthMember authMember,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuRequest menuRequest
    ) {
        ApiResponse<MenuResponse> result = menuService.updateMenu(authMember, storeId, menuId, menuRequest);
        return ApiResponse.of(result, result.getData());
    }
}
