package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseMenuEnum implements ApiResponseEnumImpl {
    // 200
    MENU_SAVE_SUCCESS(HttpStatus.OK, "메뉴 저장에 성공하였습니다."),
    MENU_UPDATE_SUCCESS(HttpStatus.OK, "메뉴 수정에 성공하였습니다."),

    // 400
    MENU_MAX(HttpStatus.BAD_REQUEST,"입력된 정보가 틀렸습니다."),

    // 403
    NOT_OWNER(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseMenuEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
