package com.sparta.deliveryapp.domain.store.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMenuEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.model.StoreResponseDto;
import com.sparta.deliveryapp.domain.store.model.StoresResponseDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
//    private final MemberRepository memberRepository;
//    private final MemberService memberService;

    // TODO: 멤버 연관관계 추가해야함
    @Transactional
    public ApiResponse<Void> createStore(
            // memberId,
            StoreRequestDto requestDto
    ) {
//        Member member =  memberRepository.findById(memberId).orElseThrow( () -> new HandleNotFound(...) );
//        if (member.role.equals(UserRole.USER) ) {
//            throw new HandleUnauthorized(ApiResponseStoreEnum.NOT_OWNER)
//        }
//        int storeCount = getStoreCount(memberId);
//        if (storeCount >= 3) {
//            throw new HandleMaxException(ApiResponseStoreEnum.STORE_MAX);
//        }
        Store store = Store.of(requestDto);
        storeRepository.save(store);
        return ApiResponse.ofApiResponseEnum(ApiResponseStoreEnum.STORE_SAVE_SUCCESS);
    }

    // TODO: 수정할때 가게만든 사장이랑, 로그인한 사장이 일치하는지 확인해야함
    @Transactional
    public ApiResponse<Void> updateStore(Long storeId, StoreRequestDto storeRequestDto) {
        Store store = findByStoreId(storeId);
        Store updateStore = Store.builder()
                .id(store.getId())
                .storeName(Objects.isNull(storeRequestDto.getStoreName()) ? store.getStoreName() : storeRequestDto.getStoreName() )
                .openingTime(Objects.isNull(storeRequestDto.getOpeningTime()) ? store.getOpeningTime() : storeRequestDto.getOpeningTime() )
                .closingTime(Objects.isNull(storeRequestDto.getClosingTime()) ? store.getClosingTime() : storeRequestDto.getClosingTime() )
                .minOrderAmount(Objects.isNull(storeRequestDto.getMinOrderAmount()) ? store.getMinOrderAmount() : storeRequestDto.getMinOrderAmount() )
                .isClose(false)
                .build();
        storeRepository.save(updateStore);
        return ApiResponse.ofApiResponseEnum(ApiResponseStoreEnum.STORE_UPDATE_SUCCESS);
    }

    @Transactional
    public ApiResponse<StoreResponseDto> getStore(Long storeId) {
        Store store = findByStoreId(storeId);
        Menu menu = menuRepository.findByStore(store).orElseThrow(() -> new HandleNotFound(ApiResponseMenuEnum.MENU_NOT_FOUND));
        StoreResponseDto storeResponseDto = StoreResponseDto.of(store,menu);
        return ApiResponse.ofApiResponseEnum(ApiResponseStoreEnum.STORE_GET_SUCCESS,storeResponseDto);
    }

    /**
     *
     *
     * @param storeName
     * @param pageable
     * @return
     */
    @Transactional
    public ApiResponse<List<StoresResponseDto>> getStores(String storeName, Pageable pageable) {
        if (Strings.isBlank(storeName)) {
            Page<Store> page = findAllStorePage(pageable);
            List<StoresResponseDto> storesResponse = page.map(StoresResponseDto::of).toList();
            return ApiResponse.ofApiResponseEnum(ApiResponseStoreEnum.STORE_GET_SUCCESS,storesResponse);
        } else {
            Page<Store> page = findAllByStoreNameAndPage(storeName,pageable);
            List<StoresResponseDto> storesResponse = page.map(StoresResponseDto::of).toList();
            return ApiResponse.ofApiResponseEnum(ApiResponseStoreEnum.STORE_GET_SUCCESS,storesResponse);
        }
    }

    /**
     * storeId로 가게 찾는 메서드
     *
     * @param storeId 가게 찾을 storeId
     * @return 조회된 store 반환
     */
    @Transactional(readOnly = true)
    public Store findByStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new HandleNotFound(ApiResponseStoreEnum.STORE_NOT_FOUND));
    }

    /**
     *  storeName이 포함되는 가게 전부 찾기, 없으면 빈 List 반환
     * @param storeName 찾을 가게명
     * @param pageable 조회 하고자 하는 페이지 정보
     * @return 가게명이 포함되는 가게 전부 반환 (자음 + 모음 합친것만 가능)
     */
    @Transactional(readOnly = true)
    public Page<Store> findAllByStoreNameAndPage(String storeName, Pageable pageable) {
        return storeRepository.findAllByStoreNameContaining(storeName,pageable);
    }

    /**
     * storeName이 공백일떄는 모든 가게를 반환시키기 위한 메서드
     * @param pageable 조회 하고자 하는 페이지 정보
     * @return 모든 가게 반환
     */
    @Transactional(readOnly = true)
    public Page<Store> findAllStorePage(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }



    /**
     * 사장 id로 가게 몇개 개설했는지 찾는 메서드
     *
     * @param memberId 가게 갯수 셀 사장의 id
     * @return 조회된 가게들 갯수 반환
     */
//    @Transactional(readOnly = true)
//    public Integer getStoreCount(Long memberId) {
//        List<Store> stores = storeRepository.findALlByMemberId(memberId);
//        return stores.size();
//    }
}
