package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseStoreEnum implements ApiResponseEnumImpl {
    // 200
    STORE_SAVE_SUCCESS(HttpStatus.OK.value(), "가게 저장에 성공 하였습니다"),
    STORE_UPDATE_SUCCESS(HttpStatus.OK.value(), "가게 업데이트에 성공 하였습니다"),
    STORE_GET_SUCCESS(HttpStatus.OK.value(), "가게 조회에 성공 하였습니다"),
    STORE_CLOSE_SUCCESS(HttpStatus.OK.value(), "가게 폐업에 성공 하였습니다"),

    // 400
    STORE_MAX(HttpStatus.BAD_REQUEST.value(),"가게는 3개까지 만들 수 있습니다"),

    // 403
    NOT_OWNER(HttpStatus.FORBIDDEN.value(), "사장 권한이 아닙니다"),

    // 404
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "가게를 찾을 수 없습니다");

    private final int code;
    private final String message;

    ApiResponseStoreEnum(int code,String message) {
        this.message = message;
        this.code = code;
    }
}
