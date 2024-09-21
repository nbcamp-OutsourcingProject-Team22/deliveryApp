package com.sparta.deliveryapp.exception;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(ApiResponseEnumImpl apiResponseEnum) {
        super(apiResponseEnum);
    }
}
