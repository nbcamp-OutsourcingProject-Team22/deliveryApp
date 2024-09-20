package com.sparta.deliveryapp.domain.member.service;

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


    public void signup(SignupRequest request) {
        Optional<Members> userByEmail = memberRepository.findByEmail(request.getEmail());
        Optional<Members> userByNickname = memberRepository.findByUsername(request.getUsername());

        if (userByEmail.isPresent()) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다");
        }

        if (userByNickname.isPresent()) {
            throw new InvalidRequestException("이미 존재하는 이름입니다");
        }

        if (!PasswordUtils.isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 대소문자 영문, 숫자, 특수문자를 각각 1글자 이상 포함하고, 최소 8글자 이상이어야 합니다.");
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
    }
}
