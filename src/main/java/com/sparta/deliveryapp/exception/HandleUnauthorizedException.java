package com.sparta.deliveryapp.exception;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;

/**
 * 인증,인가 되지않았을때 발생되는 예외
 */
public class HandleUnauthorizedException extends BaseException {
    public HandleUnauthorizedException(ApiResponseEnumImpl apiResponseEnum) {
        super(apiResponseEnum);
    }
}
