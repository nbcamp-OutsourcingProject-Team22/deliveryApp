package com.sparta.deliveryapp.domain.member.controller;

import com.sparta.deliveryapp.domain.dto.request.SignupRequest;
import com.sparta.deliveryapp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> signup (@RequestBody SignupRequest request){
        try {
            memberService.signup(request);
            return new ResponseEntity<>("화원가입 완료", HttpStatus.OK);
        }catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
