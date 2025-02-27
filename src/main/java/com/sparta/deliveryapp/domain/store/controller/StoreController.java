package com.sparta.deliveryapp.domain.store.controller;

import com.sparta.deliveryapp.annotation.Auth;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.model.StoreResponseDto;
import com.sparta.deliveryapp.domain.store.model.StoresResponseDto;
import com.sparta.deliveryapp.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>>  createStore(
            @Auth AuthMember authMember,
            @RequestBody @Valid StoreRequestDto storeRequestDto
    ) {
        long memberId = authMember.getId();
        ApiResponse<Void> result = storeService.createStore(memberId,storeRequestDto);
        return ApiResponse.of(result);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Void>>  updateStore(
            @Auth AuthMember authMember,
            @PathVariable Long storeId,
            @RequestBody StoreRequestDto storeRequestDto
    ) {
        Long memberId = authMember.getId();
        ApiResponse<Void> result = storeService.updateStore(memberId,storeId, storeRequestDto);
        return ApiResponse.of(result);
    }

    /**
     * 가게 단건 조회
     * @param storeName 조회할 가게
     * @return 조회된 가게 반환 (메뉴포함, 없으면 null 처리)
     */
    @GetMapping("/{storeName}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> getStore(
            @PathVariable String storeName
    ) {
        ApiResponse<StoreResponseDto> result = storeService.getStore(storeName);
        return ApiResponse.of(result);
    }

    /**
     * 고객이 검색한 입력어가 없으면, 모든 가게가 조회되고 검색어가 있다면 해당 검색어가 포함된 가게만 표출됨
     *
     * @param storeName 검색한 가게명
     * @param page 몇 페이지
     * @param size 몇개
     * @param sort 가게 생성일 기준 정렬여부(오름차순,내림차순)
     * @return 가게이름과 일치하는 가게 반환, 없으면 빈 List 반환
     */
    @GetMapping("/query")
    public ResponseEntity<ApiResponse<List<StoresResponseDto>>> getStores(
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "desc") String sort
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page - 1, size, direction,"createdAt");
        ApiResponse<List<StoresResponseDto>> result = storeService.getStores(storeName,pageable);
        return ApiResponse.of(result);
    }

    /**
     * 가게 폐업 메서드
     * @param storeId 폐업할 가게
     * @return 폐업성공 메세지
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Void>> closeStore(
            @Auth AuthMember authMember,
            @PathVariable Long storeId
    ) {
        Long memberId = authMember.getId();
        ApiResponse<Void> result = storeService.closeStore(memberId,storeId);
        return ApiResponse.of(result);
    }
}
