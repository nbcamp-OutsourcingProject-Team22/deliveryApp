package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private T data;

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 상태코드, 메세지만 반환시 사용
    public static ResponseEntity<ApiResponse<Void>> of(ApiResponseEnumImpl apiResponseEnum) {
        return ResponseEntity.status(apiResponseEnum.getCode())
                .body(new ApiResponse<>(apiResponseEnum.getCode(), apiResponseEnum.getMessage()));
    }

    // 데이터, 상태코드, 메세지만 반환시 사용
    public static <T> ResponseEntity<ApiResponse<T>> of(ApiResponseEnumImpl apiResponseEnum, T data) {
        return ResponseEntity.status(apiResponseEnum.getCode())
                .body(new ApiResponse<>(apiResponseEnum.getCode(), apiResponseEnum.getMessage(),data));
    }
}
