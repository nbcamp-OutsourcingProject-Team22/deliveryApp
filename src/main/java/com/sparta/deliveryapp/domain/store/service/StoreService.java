package com.sparta.deliveryapp.domain.store.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
//    private final MemberRepository memberRepository;
//    private final MemberService memberService;

    // TODO: 멤버 연관관계 추가해야함
    @Transactional
    public ApiResponseEnumImpl createStore(
            // memberId,
            StoreRequestDto requestDto
    ) {
//        Member member =  memberRepository.findById(memberId).orElseThrow( () -> new HandleNotFound(...) );
//        if (member.role == UserRole.USER) {
//            throw new HandleUnauthorized(ApiResponseStoreEnum.NOT_OWNER)
//        }
//        int storeCount = getStoreCount(memberId);
//        if (storeCount >= 3) {
//            throw new HandleMaxException(ApiResponseStoreEnum.STORE_MAX);
//        }
        Store store = Store.of(requestDto);
        storeRepository.save(store);
        return ApiResponseStoreEnum.STORE_SAVE_SUCCESS;
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