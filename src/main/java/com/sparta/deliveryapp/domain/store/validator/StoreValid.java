package com.sparta.deliveryapp.domain.store.validator;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;

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
}
