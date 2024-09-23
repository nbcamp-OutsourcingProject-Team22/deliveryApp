package com.sparta.deliveryapp.domain.store.validator;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.entity.Members;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;

public class StoreValid {

    /**
     * 폐업 여부 확인
     * @param store 확인할 가게
     * @throws HandleNotFound 가게가 폐업 상태일때 발생되는 예외
     */
    public static void isCloseStore(Store store) {
        if (store.getIsClose() ) {
            throw new HandleNotFound(ApiResponseStoreEnum.STORE_NOT_FOUND_CLOSE);
        }
    }
    /**
     * 가게의 사장인지 확인
     * @param realOwner (현재 내 가게의 실제 사장) ,inOwner (클라이언트 부터 요청 들어온 사장)
     * @throws HandleUnauthorizedException 가게의 사장이 아닐경우 발생되는 예외
     */
    public static void isOwnerStore(Members realOwner, Members otherOwner) {
        // 가게의 사장이 아니라면
        if(!(realOwner.getId() == otherOwner.getId())) {
            throw new HandleUnauthorizedException(ApiResponseStoreEnum.STORE_NOT_OWNER);
        }
    }
}
