package com.sparta.deliveryapp.domain.member.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMemberEnum;
import com.sparta.deliveryapp.config.PasswordEncoder;
import com.sparta.deliveryapp.config.PasswordUtils;
import com.sparta.deliveryapp.domain.dto.request.SignupRequest;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.entity.Members;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public ApiResponseMemberEnum signup(SignupRequest request) {
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
       return ApiResponseMemberEnum.MEMBER_SAVE_SUCCESS;
    }

    public ApiResponseMemberEnum userSignup(SignupRequest request) {
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
        return ApiResponseMemberEnum.MEMBER_SAVE_SUCCESS;
    }
}
