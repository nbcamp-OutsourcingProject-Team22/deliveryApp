package com.sparta.deliveryapp.domain.menu.controller;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/{storeId}")
public class MenuController {
    private final MenuService menuService;
    private final MenuRepository menuRepository;

    // 메뉴 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMenu (
//            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody @Valid MenuRequest menuRequest){
//       Long storeId = authUser.getId();
        ApiResponse<Void> result = menuService.createMenu(storeId, menuRequest);
//        String test = "실험중";
        return ApiResponse.of(result);
    }
}
