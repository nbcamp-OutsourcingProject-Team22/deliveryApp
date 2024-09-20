package com.sparta.deliveryapp;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;
import com.sparta.deliveryapp.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> baseException(BaseException e) {
        ApiResponseEnumImpl apiResponseEnum = e.getApiResponseEnum();
        return ResponseEntity.status(apiResponseEnum.getCode())
                .body(new ApiResponse<>(apiResponseEnum.getCode(), apiResponseEnum.getMessage()));
    }

    public ResponseEntity<ApiResponse<Object>> baseException(BaseException e, Object data) {
        ApiResponseEnumImpl apiResponseEnum = e.getApiResponseEnum();
        return ResponseEntity.status(apiResponseEnum.getCode())
                .body(new ApiResponse<>(apiResponseEnum.getCode(), apiResponseEnum.getMessage(),data));
    }

    // DTO 예외처리 커스텀하여 클라이언트로 응답 시키기
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        return ResponseEntity.status(e.getStatusCode())
                .body(new ApiResponse<>(e.getStatusCode().value(), fe.getDefaultMessage()));

    }
}
