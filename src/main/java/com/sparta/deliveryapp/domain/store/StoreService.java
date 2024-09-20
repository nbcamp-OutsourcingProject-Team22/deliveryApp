package com.sparta.deliveryapp.domain.store;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
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
