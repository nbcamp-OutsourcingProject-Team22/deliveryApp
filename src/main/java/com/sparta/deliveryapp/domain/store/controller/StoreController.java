package com.sparta.deliveryapp.domain.store.controller;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    // TODO: 사용자 받아야함 @Auth 같은걸로
    @PostMapping
    public ResponseEntity<ApiResponse<Void>>  createStore(
//            @Auth AuthUser authUser;
            @RequestBody @Valid StoreRequestDto storeRequestDto
    ) {
//        Long memberId = authUser.getId();
        ApiResponseEnumImpl result = storeService.createStore(storeRequestDto);
        return ApiResponse.of(result);
    }
}
