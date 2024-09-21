package com.sparta.deliveryapp.apiResponseEnum;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private T data;

    // 상태코드, 메시지 반환 생성자 (ApiResponseEnumImpl)
    public ApiResponse(ApiResponseEnumImpl apiResponseEnum) {
        this.code = apiResponseEnum.getCode();
        this.message = apiResponseEnum.getMessage();
    }

    // 데이터, 상태코드, 메시지 반환 생성자 (ApiResponseEnumImpl)
    public ApiResponse(ApiResponseEnumImpl apiResponseEnum, T data) {
        this.code = apiResponseEnum.getCode();
        this.message = apiResponseEnum.getMessage();
        this.data = data;
    }

    // 상태코드, 메시지 반환 생성자 (code,message)
    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 데이터, 상태코드, 메시지 반환 생성자 (data,code,message)
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 상태코드, 메세지만 반환시 사용 (서비스 -> 컨트롤러)
    public static ApiResponse<Void> ofApiResponseEnum(ApiResponseEnumImpl apiResponseEnum) {
        return new ApiResponse<>(apiResponseEnum);
    }

    // 데이터, 상태코드, 메세지만 반환시 사용 (서비스 -> 컨트롤러)
    public static <T> ApiResponse<T> ofApiResponseEnum(ApiResponseEnumImpl apiResponseEnum, T data) {
        return new ApiResponse<>(apiResponseEnum,data);
    }

    // 상태코드, 메세지만 반환시 사용 (컨트롤러 -> 클라이언트)
    public static ResponseEntity<ApiResponse<Void>> of(ApiResponse<Void> apiResponse) {
        return ResponseEntity.status(apiResponse.getCode())
                .body(new ApiResponse<>(apiResponse.getCode(),apiResponse.message));
    }

    // 데이터, 상태코드, 메세지만 반환시 사용 (컨트롤러 -> 클라이언트)
    public static <T> ResponseEntity<ApiResponse<T>> of(ApiResponse<T> apiResponse, T data) {
        return ResponseEntity.status(apiResponse.getCode())
                .body(new ApiResponse<>(apiResponse.getCode(),apiResponse.getMessage(),data));
    }
}
