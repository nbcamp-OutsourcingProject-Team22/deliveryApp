package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseReviewEnum implements ApiResponseEnumImpl {
    // 200
    REVIEW_SAVE_SUCCESS(HttpStatus.OK, "리뷰 작성이 성공하였습니다."),
    REVIEW_GET_STORE_SUCCESS(HttpStatus.OK, "가게 별 리뷰 조회 성공하였습니다."),
    REVIEW_GET_RATING_SUCCESS(HttpStatus.OK, "별점 별 리뷰 조회 성공하였습니다.”"),

    // 400
    EMPTY_STORE_REVIEW(HttpStatus.BAD_REQUEST,"해당 가게의 리뷰가 존재하지않습니다."),
    EMPTY_RATING_REVIEW(HttpStatus.BAD_REQUEST,"해당 별점의 리뷰는 존재하지않습니다."),

    // 401
    UNAUTHORIZED_ORDER_STATUS(HttpStatus.UNAUTHORIZED,"배달이 완료되지 않았습니다."),

    // 404
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseReviewEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
