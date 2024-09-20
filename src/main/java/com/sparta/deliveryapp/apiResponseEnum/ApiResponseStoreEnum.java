package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseStoreEnum implements ApiResponseEnumImpl {
    // 200
    STORE_SAVE_SUCCESS(HttpStatus.OK, "가게 저장에 성공 하였습니다"),

    // 400
    STORE_MAX(HttpStatus.BAD_REQUEST,"가게는 3개까지 만들 수 있습니다"),

    // 403
    NOT_OWNER(HttpStatus.FORBIDDEN, "사장 권한이 아닙니다"),

    // 404
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseStoreEnum(HttpStatus httpStatus,String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
