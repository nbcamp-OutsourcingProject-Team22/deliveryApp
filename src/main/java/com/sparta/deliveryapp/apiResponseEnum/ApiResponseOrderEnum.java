package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseOrderEnum implements ApiResponseEnumImpl {
    // 200
    ORDER_SAVE_SUCCESS(HttpStatus.OK, "주문 저장에 성공하였습니다."),
    ORDER_CHECK_SUCCESS(HttpStatus.OK, "주문 조회에 성공하였습니다."),
    ORDER_ACCEPT_SUCCESS(HttpStatus.OK, "주문 수락에 성공하였습니다."),
    ORDER_REJECT_SUCCESS(HttpStatus.OK, "주문 거절에 성공하였습니다."),
    ORDER_PROCEED_SUCCESS(HttpStatus.OK, "주문 진행에 성공하였습니다."),

    // 400
    ORDER_COMPLETE(HttpStatus.BAD_REQUEST,"완료된 주문입니다."),
    ORDER_REJECT(HttpStatus.BAD_REQUEST,"거부된 주문입니다."),
    ORDER_IN_PROGRESS(HttpStatus.BAD_REQUEST,"진행중인 주문입니다."),

    // 403
    NOT_OWNER(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseOrderEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
