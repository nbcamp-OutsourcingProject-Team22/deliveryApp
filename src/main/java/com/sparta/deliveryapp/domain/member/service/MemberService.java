package com.sparta.deliveryapp.domain.member.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseEnumImpl;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMemberEnum;
import com.sparta.deliveryapp.config.PasswordEncoder;
import com.sparta.deliveryapp.config.PasswordUtils;
import com.sparta.deliveryapp.domain.dto.SignInRequestDto;
import com.sparta.deliveryapp.domain.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.entity.Members;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import com.sparta.deliveryapp.jwt.JwtUtil;
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
        Optional<Members> userByEmail = memberRepository.findByEmail(request.getEmail());
        Optional<Members> userByNickname = memberRepository.findByUsername(request.getUsername());

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


        Members newMember = new Members(
                request.getEmail(),
                request.getUsername(),
                encodedPassword,
                userRole
        );


       memberRepository.save(newMember);
       return ApiResponse.ofApiResponseEnum(ApiResponseMemberEnum.MEMBER_SAVE_SUCCESS);
    }

    public ApiResponse<Void> userSignup(SignupRequestDto request) {
        Optional<Members> userByEmail = memberRepository.findByEmail(request.getEmail());
        Optional<Members> userByNickname = memberRepository.findByUsername(request.getUsername());

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


        Members newMember = new Members(
                request.getEmail(),
                request.getUsername(),
                encodedPassword,
                userRole
        );

        memberRepository.save(newMember);
        return ApiResponse.ofApiResponseEnum(ApiResponseMemberEnum.MEMBER_SAVE_SUCCESS);
    }

    public String signIn(SignInRequestDto requestDto) {
        //email 조회
        Members member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //비밀번호 일치 확인
        if (!passwordEncoder.matches(requestDto.getPassword(),member.getPassword() )){
            throw new IllegalArgumentException("비밀번호를 확인해주세요");
        }

        return jwtUtil.createToken(member.getId(), member.getUserRole());
    }
}
