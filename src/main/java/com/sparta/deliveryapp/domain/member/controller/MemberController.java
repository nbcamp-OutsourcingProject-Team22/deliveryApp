package com.sparta.deliveryapp.domain.member.controller;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;
import com.sparta.deliveryapp.domain.dto.SignInRequestDto;
import com.sparta.deliveryapp.domain.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //owner 회원가입
    @PostMapping("/owner/sign-up")
    public ResponseEntity<ApiResponse<Void>> signup (@RequestBody SignupRequestDto request){

        ApiResponse<Void> result = memberService.signup(request);
        return ApiResponse.of(result);
    }

    //user 회원가입
    @PostMapping("/user/sign-up")
    public ResponseEntity<ApiResponse<Void>> userSignup (@RequestBody SignupRequestDto request){
        ApiResponse<Void> result =  memberService.userSignup(request);
        return ApiResponse.of(result);
    }

    //owner, user login
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInRequestDto requestDto){
                String token = memberService.signIn(requestDto);
                return ResponseEntity.ok(token);
    }
}
