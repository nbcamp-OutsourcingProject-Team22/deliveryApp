package com.sparta.deliveryapp.domain.member.service;


import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMemberEnum;
import com.sparta.deliveryapp.config.PasswordEncoder;
import com.sparta.deliveryapp.config.PasswordUtils;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.dto.request.SecessionRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignInRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import com.sparta.deliveryapp.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public ApiResponse<Void> signup(SignupRequestDto request) {
        Optional<Member> userByEmail = memberRepository.findByEmail(request.getEmail());
        Optional<Member> userByNickname = memberRepository.findByUsername(request.getUsername());

        if (userByEmail.isPresent()) {
            throw new InvalidRequestException(ApiResponseMemberEnum.USERNAME_EMAIL_CHECK);
        }

        if (userByNickname.isPresent()) {
            throw new InvalidRequestException(ApiResponseMemberEnum.USERNAME_EMAIL_CHECK);
        }

        if (!PasswordUtils.isValidPassword(request.getPassword())) {
            throw new InvalidRequestException(ApiResponseMemberEnum.PASSWORD_CHECK);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserRole userRole = UserRole.OWNER;


        Member newMember = new Member(
                request.getEmail(),
                request.getUsername(),
                encodedPassword,
                userRole
        );


        memberRepository.save(newMember);
        return ApiResponse.ofApiResponseEnum(ApiResponseMemberEnum.MEMBER_SAVE_SUCCESS);
    }

    public ApiResponse<Void> userSignup(SignupRequestDto request) {
        Optional<Member> userByEmail = memberRepository.findByEmail(request.getEmail());
        Optional<Member> userByNickname = memberRepository.findByUsername(request.getUsername());

        if (userByEmail.isPresent()) {
            throw new InvalidRequestException(ApiResponseMemberEnum.USERNAME_EMAIL_CHECK);
        }

        if (userByNickname.isPresent()) {
            throw new InvalidRequestException(ApiResponseMemberEnum.USERNAME_EMAIL_CHECK);
        }

        if (!PasswordUtils.isValidPassword(request.getPassword())) {
            throw new InvalidRequestException(ApiResponseMemberEnum.PASSWORD_CHECK);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserRole userRole = UserRole.USER;


        Member newMember = new Member(
                request.getEmail(),
                request.getUsername(),
                encodedPassword,
                userRole
        );

        memberRepository.save(newMember);
        return ApiResponse.ofApiResponseEnum(ApiResponseMemberEnum.MEMBER_SAVE_SUCCESS);
    }

    public String signIn(SignInRequestDto requestDto, HttpServletResponse response) {
        //email 조회
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //비밀번호 일치 확인
        if (!passwordEncoder.matches(requestDto.getPassword(),member.getPassword() )){
            throw new IllegalArgumentException("비밀번호를 확인해주세요");
        }
        String token = jwtUtil.createToken(member.getId(), member.getUsername(), member.getUserRole(), member.isActive(), member.isSecession());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return token;
    }

    public ApiResponse<Void> secession(AuthMember authMember, SecessionRequestDto requestDto){

        //멤버 확인
        Member member = memberRepository.findById(authMember.getId()).orElseThrow(() ->
                new NullPointerException("잘못된 정보입니다."));


        //비밀번호 확인
        if (!PasswordUtils.isValidPassword(requestDto.getPassword())) {
            throw new InvalidRequestException(ApiResponseMemberEnum.PASSWORD_CHECK);
        }

        member.changeSecession();
        memberRepository.save(member);

        return ApiResponse.ofApiResponseEnum(ApiResponseMemberEnum.MEMBER_SECESSION);
    }
}
