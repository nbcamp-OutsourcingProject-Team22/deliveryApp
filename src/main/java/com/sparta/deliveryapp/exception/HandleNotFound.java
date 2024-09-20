package com.sparta.deliveryapp.exception;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;

/**
 *  무언가 못찾을때 발생되는 예외
 */
public class HandleNotFound extends BaseException {
    public HandleNotFound(ApiResponseEnumImpl apiResponseEnum) {
        super(apiResponseEnum);
    }
}
