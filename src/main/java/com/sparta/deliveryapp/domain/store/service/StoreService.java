package com.sparta.deliveryapp.domain.store.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMemberEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.model.StoreResponseDto;
import com.sparta.deliveryapp.domain.store.model.StoresResponseDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Members;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleMaxException;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
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
    private final MemberRepository memberRepository;

    // TODO: 멤버 연관관계 추가해야함
    @Transactional
    public ApiResponse<Void> createStore(Integer memberId,StoreRequestDto requestDto) {
        // 여기 바꿔야함
        Members member = memberRepository.findById(memberId).orElseThrow( () -> new HandleNotFound(ApiResponseMemberEnum.PASSWORD_UNAUTHORIZED));

        if (member.getUserRole().equals(UserRole.USER) ) {
            throw new HandleUnauthorizedException(ApiResponseStoreEnum.NOT_OWNER);
        }
        int storeCount = getStoreCount(member);
        if (storeCount >= 3) {
            throw new HandleMaxException(ApiResponseStoreEnum.STORE_MAX);
        }
        Store store = Store.of(requestDto);
        store.addMember(member);
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

    /**
     *  단건 가게 반환 (철자 정확히 일치해야함)
     * @param storeName 찾고자 하는 가게
     * @return 조회된 가게 반환 (메뉴 있으면 메뉴 포함하여 반환)
     */
    @Transactional
    public ApiResponse<StoreResponseDto> getStore(String storeName) {
        Store store = findByStoreName(storeName);
        store.isClosed();
        Menu menu = menuRepository.findByStore(store).orElse(null);
        StoreResponseDto storeResponseDto;
        if (menu == null) {
            // 메뉴가 없다면 menu는 null로 처리하기위하여, store만 넣어줌
            storeResponseDto = StoreResponseDto.of(store);
        } else {
            // 메뉴가 있다면 메뉴 넣어서 처리
            storeResponseDto = StoreResponseDto.of(store,menu);
        }

        return ApiResponse.ofApiResponseEnum(ApiResponseStoreEnum.STORE_GET_SUCCESS,storeResponseDto);
    }

    /**
     * 클라이언트로부터 들어온 문자열과, 일부 일치하는 가게 전부 반환 메서드
     *
     * @param storeName 조회할 가게 이름
     * @param pageable 페이지 정보 (page,size,sort)
     * @return 조회된 가게들 반환
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
     * 가게 폐업 메서드
     * @param storeId 폐업할 가게
     * @return 폐업 성공 내용 반환
     */
    @Transactional
    public ApiResponse<Void> closeStore(Long storeId) {
        Store store = findByStoreId(storeId);
        // 폐업 전환
        store.closed();
        return new ApiResponse<>(ApiResponseStoreEnum.STORE_CLOSE_SUCCESS);
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
     * storeName로 가게 찾는 메서드
     *
     * @param storeName 가게 찾을 storeName
     * @return 조회된 store 반환
     */
    @Transactional(readOnly = true)
    public Store findByStoreName(String storeName) {
        return storeRepository.findByStoreName(storeName).orElseThrow(() -> new HandleNotFound(ApiResponseStoreEnum.STORE_NOT_FOUND));
    }

    /**
     *  storeName이 포함되는 가게 전부 찾기,false인것들만 반환함, 없으면 빈 List 반환
     * @param storeName 찾을 가게명
     * @param pageable 조회 하고자 하는 페이지 정보
     * @return 가게명이 포함되는 가게 전부 반환 (자음 + 모음 합친것만 가능)
     */
    @Transactional(readOnly = true)
    public Page<Store> findAllByStoreNameAndPage(String storeName, Pageable pageable) {
        return storeRepository.findAllByStoreNameContainingAndIsCloseFalse(storeName,pageable);
    }

    /**
     * storeName이 공백일때는 모든 가게를 반환하는데, close 여부가 false인것들만 반환함
     * @param pageable 조회 하고자 하는 페이지 정보
     * @return 모든 가게 반환
     */
    @Transactional(readOnly = true)
    public Page<Store> findAllStorePage(Pageable pageable) {
        return storeRepository.findAllByIsCloseFalse(pageable);
    }

    /**
     * 사장 id로 가게 몇개 개설했는지, 정상 운영중인 가게들만 가져옴
     *
     * @param member 가게 갯수 셀 사장
     * @return 조회된 가게들 갯수 반환
     */
    @Transactional(readOnly = true)
    public Integer getStoreCount(Members member) {
        List<Store> stores = storeRepository.findAllByMemberAndIsCloseFalse(member);
        return stores.size();
    }
}
