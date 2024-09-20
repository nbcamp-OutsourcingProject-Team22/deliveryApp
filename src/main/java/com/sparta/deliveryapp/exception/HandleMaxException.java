package com.sparta.deliveryapp.exception;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;

/**
 * 무언가 초과 됐을때 사용되는 예외처리
 */
public class HandleMaxException extends BaseException {
    public HandleMaxException(ApiResponseEnumImpl apiResponseEnum) {
        super(apiResponseEnum);
    }
}
