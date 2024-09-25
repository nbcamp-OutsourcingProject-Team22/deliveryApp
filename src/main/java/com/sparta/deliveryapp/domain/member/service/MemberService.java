package com.sparta.deliveryapp.domain.member.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.dto.request.SecessionRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignInRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    ApiResponse<Void> signup(SignupRequestDto request);
    ApiResponse<Void> userSignup(SignupRequestDto request);
    String signIn(SignInRequestDto requestDto, HttpServletResponse response);
    ApiResponse<Void> secession(AuthMember authMember, SecessionRequestDto requestDto);
}
