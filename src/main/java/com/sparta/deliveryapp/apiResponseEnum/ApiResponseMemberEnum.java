package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseMemberEnum implements ApiResponseEnumImpl {

    MEMBER_SECESSION(HttpStatus.OK, "요청 완료"),

    // 201
    MEMBER_SAVE_SUCCESS(HttpStatus.CREATED, "회원가입 완료"),

    // 400
    PASSWORD_CHECK(HttpStatus.BAD_REQUEST,"비밀번호는 대소문자 영문, 숫자, 특수문자를 각각 1글자 이상 포함하고, 최소 8글자 이상이어야 합니다."),

    //401
    PASSWORD_UNAUTHORIZED(HttpStatus.BAD_REQUEST,"비밀번호를 확인해주세요."),

    //404
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"멤버를 찾을 수 없습니다."),

    // 409
    USERNAME_EMAIL_CHECK(HttpStatus.CONFLICT, "이미 존재하는 email 혹은 username입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseMemberEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
