package com.sparta.deliveryapp.exception;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    ApiResponseEnumImpl apiResponseEnum;
    public BaseException(ApiResponseEnumImpl apiResponseEnum) {
        this.apiResponseEnum = apiResponseEnum;
    }
}
