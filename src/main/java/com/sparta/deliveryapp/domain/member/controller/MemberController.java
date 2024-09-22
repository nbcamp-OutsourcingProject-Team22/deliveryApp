package com.sparta.deliveryapp.domain.member.controller;

import com.sparta.deliveryapp.annotation.Auth;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.dto.request.SignInRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.response.AuthInfoResponseDto;
import com.sparta.deliveryapp.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //owner 회원가입
    @PostMapping("/owner/sign-up")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequestDto request) {
        ApiResponse<Void> result = memberService.signup(request);
        return ApiResponse.of(result);
    }

    //user 회원가입
    @PostMapping("/user/sign-up")
    public ResponseEntity<ApiResponse<Void>> userSignup(@RequestBody SignupRequestDto request) {
        ApiResponse<Void> result = memberService.userSignup(request);
        return ApiResponse.of(result);
    }

    //owner, user login
    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody SignInRequestDto requestDto, HttpServletResponse response) {
        String token = memberService.signIn(requestDto, response);

        //header 저장
        response.setHeader("Authorization", token);
        return ResponseEntity.ok().build();
    }
}
