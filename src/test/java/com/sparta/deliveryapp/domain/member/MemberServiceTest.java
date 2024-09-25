package com.sparta.deliveryapp.domain.member;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.config.PasswordEncoder;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.dto.request.SecessionRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignInRequestDto;
import com.sparta.deliveryapp.domain.member.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.member.service.MemberServiceImpl;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import com.sparta.deliveryapp.init.TestInfo;
import com.sparta.deliveryapp.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JwtUtil jwtUtil; //문제 1

    @InjectMocks
    private MemberServiceImpl memberService;

    private SignInRequestDto signInRequestDto;
    private Member existingMember;

    @Mock
    SignupRequestDto signupRequestDto;
    SecessionRequestDto secessionRequestDto;
    Member ownerMember;

    @Spy
    HttpServletResponse response; //문제4

    String testMemberUsername1 = "test";
    String testMemberEmail1 = "test@naver.com";
    String testMemberPassword1 = "!@Skdud340";

    Long testMemberId1 = 1L;

    @BeforeEach
    public void setup() {
        signupRequestDto = TestInfo.getOneSignUpRequestDto();
        ownerMember = TestInfo.getOwnerOneMember();
        ReflectionTestUtils.setField(ownerMember, "username", testMemberUsername1);
        ReflectionTestUtils.setField(ownerMember, "email", testMemberEmail1);
        ReflectionTestUtils.setField(ownerMember, "password", testMemberPassword1);

        //문제2
        ReflectionTestUtils.setField(jwtUtil,"secretKey","7ZWt7ZW0OTntmZTsnbTtjIXtlZzqta3snYTrhIjrqLjshLjqs4TroZzrgpjslYTqsIDsnpDtm4zrpa3tlZzqsJzrsJzsnpDrpbzrp4zrk6TslrTqsIDsnpA=");
        jwtUtil.init();

        existingMember = TestInfo.getOwnerOneMember();
        signInRequestDto = new SignInRequestDto("test1@naver.com", "!@Skdud340");
        secessionRequestDto = new SecessionRequestDto("!@Skdud340");

        //문제 3
        memberService = new MemberServiceImpl(memberRepository,passwordEncoder,jwtUtil);
    }


    @Nested
    class 회원_가입 {
        @Test
        @DisplayName("role = owner인 멤버 생성 성공")
        void create_user_test() {

            //given - email, username, password
            SignupRequestDto requestDto = new SignupRequestDto("test@gmail.com", "첫 onwer", "Asdf1234!");

            given(memberRepository.findByEmail("test@gmail.com")).willReturn(Optional.empty());
            given(memberRepository.findByUsername("첫 onwer")).willReturn(Optional.empty());
            given(passwordEncoder.encode("Asdf1234!")).willReturn("encodedPassword");

            //when - 회원가입 시도
            ApiResponse<Void> result = memberService.signup(requestDto);

            //then
            assertEquals("회원가입 완료", result.getMessage());
        }

        @Test
        @DisplayName("role = user인 멤버 생성 성공")
        void create_user_role_test() {
            //given - email, username, password
            SignupRequestDto requestDto = new SignupRequestDto("test1@gmail.com", "첫 user", "Asdf1234!");

            given(memberRepository.findByEmail("test1@gmail.com")).willReturn(Optional.empty());
            given(memberRepository.findByUsername("첫 user")).willReturn(Optional.empty());
            given(passwordEncoder.encode("Asdf1234!")).willReturn("encodedPassword");

            //when - 회원가입 시도
            ApiResponse<Void> result = memberService.userSignup(requestDto);

            //then
            assertEquals("회원가입 완료", result.getMessage());
        }

        @Test
        @DisplayName("회원가입실패 : 중복된 email")
        void fail_to_signup() {
            //given - email, username, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            Member existingMember = TestInfo.getOwnerOneMember();

            given(memberRepository.findByEmail(signupRequestDto.getEmail())).willReturn(Optional.of(existingMember));
            given(memberRepository.findByUsername(signupRequestDto.getUsername())).willReturn(Optional.empty());

            // when -  회원가입 시도
            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                memberService.signup(signupRequestDto);
            });

            // then
            assertEquals("이미 존재하는 email 혹은 username입니다.", exception.getApiResponseEnum().getMessage());
        }

        @Test
        @DisplayName("회원가입 실패: 중복된 username")
        void fail_to_signup_with_duplicate_username() {
            //given - email, username, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            Member existingMember = TestInfo.getOwnerOneMember();

            given(memberRepository.findByUsername(signupRequestDto.getUsername())).willReturn(Optional.of(existingMember));

            // when -  회원가입 시도
            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                memberService.signup(signupRequestDto);
            });

            // then
            assertEquals("이미 존재하는 email 혹은 username입니다.", exception.getApiResponseEnum().getMessage());
        }

        @Test
        @DisplayName("비밀번호 최소 자리수 이하")
        void fail_password_length() {

            SignupRequestDto signupRequestDto = new SignupRequestDto("test@example.com", "testuser", "Short1!");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                memberService.signup(signupRequestDto);
            });

            assertEquals("비밀번호는 대소문자 영문, 숫자, 특수문자를 각각 1글자 이상 포함하고, 최소 8글자 이상이어야 합니다.", exception.getApiResponseEnum().getMessage());
        }

        @Test
        @DisplayName("비밀번호 특수문자 누락")
        void fail_password_with_special_charactor() {
            SignupRequestDto signupRequestDto = new SignupRequestDto("test@example.com", "testuser", "Short123");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                memberService.signup(signupRequestDto);
            });

            assertEquals("비밀번호는 대소문자 영문, 숫자, 특수문자를 각각 1글자 이상 포함하고, 최소 8글자 이상이어야 합니다.", exception.getApiResponseEnum().getMessage());
        }

        @Test
        @DisplayName("비밀번호 대문자 누락")
        void fail_password_without_uppercase() {
            SignupRequestDto signupRequestDto = new SignupRequestDto("test@example.com", "testuser", "short123");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                memberService.signup(signupRequestDto);
            });

            assertEquals("비밀번호는 대소문자 영문, 숫자, 특수문자를 각각 1글자 이상 포함하고, 최소 8글자 이상이어야 합니다.", exception.getApiResponseEnum().getMessage());
        }

        @Test
        @DisplayName("비밀번호 소문자 누락")
        void fail_password_without_lowercase() {
            SignupRequestDto signupRequestDto = new SignupRequestDto("test@example.com", "testuser", "SHORT123!");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                memberService.signup(signupRequestDto);
            });

            assertEquals("비밀번호는 대소문자 영문, 숫자, 특수문자를 각각 1글자 이상 포함하고, 최소 8글자 이상이어야 합니다.", exception.getApiResponseEnum().getMessage());
        }
    }

    @Nested
    class login {
        @Test
        @DisplayName("login 성공")
        void success_login() {
            //given - email, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());


           existingMember = new Member(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getUsername(),
                    encodedPassword, // 인코딩된 비밀번호 사용
                    UserRole.OWNER
            );

            given(memberRepository.findByEmail(signInRequestDto.getEmail())).willReturn(Optional.of(existingMember));

            // when
            String result = memberService.signIn(signInRequestDto, response);

            // then
            assertNotNull(result);
            assertTrue(result.startsWith("Bearer "));
        }

        @Test
        @DisplayName("login 실패 - 찾을수 없는 email")
        void login_fail_without_email(){
            //given
            SignInRequestDto signInRequestDto = new SignInRequestDto("test@example.com",  "short123");

            //when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                memberService.signIn(signInRequestDto, response);
            });

            //then
            assertEquals("사용자를 찾을 수 없습니다.",exception.getMessage());
        }

        @Test
        @DisplayName("login 실패 - 틀린 비밀번호")
        void login_fail_wrong_password(){
            //given - email, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            String encodedPassword = passwordEncoder.encode("Asdf1234!");


            existingMember = new Member(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getUsername(),
                    encodedPassword, // 인코딩된 비밀번호 사용
                    UserRole.OWNER
            );


            given(memberRepository.findByEmail(signInRequestDto.getEmail())).willReturn(Optional.of(existingMember));

            //when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                memberService.signIn(signInRequestDto, response);
            });

            //then
            assertEquals("비밀번호를 확인해주세요",exception.getMessage());
        }
    }

    @Nested
    class Secession{
        @Test
        @DisplayName("회원탈퇴 - 성공")
        void success_secession(){
            //given - email, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());


            existingMember = new Member(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getUsername(),
                    encodedPassword, // 인코딩된 비밀번호 사용
                    UserRole.OWNER
            );

            existingMember.setId(testMemberId1);

            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(existingMember));

            //authMember
            AuthMember authMember = new AuthMember(
                    existingMember.getId(),
                    existingMember.getUsername(),
                    existingMember.getUserRole(),
                    existingMember.isActive(),
                    existingMember.isSecession()
            );

            //when
            ApiResponse<Void> result = memberService.secession(authMember, secessionRequestDto);

            assertEquals("요청 완료", result.getMessage());
        }

        @Test
        @DisplayName("회원탈퇴 - 실패")
        void fail_secession(){
            //given - email, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            String encodedPassword = passwordEncoder.encode("Asdf1234!");


            existingMember = new Member(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getUsername(),
                    encodedPassword, // 인코딩된 비밀번호 사용
                    UserRole.OWNER
            );

            existingMember.setId(testMemberId1);

            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(existingMember));

            //authMember
            AuthMember authMember = new AuthMember(
                    existingMember.getId(),
                    existingMember.getUsername(),
                    existingMember.getUserRole(),
                    existingMember.isActive(),
                    existingMember.isSecession()
            );
            existingMember.setId(testMemberId1);

            //when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                memberService.secession(authMember, secessionRequestDto);
            });

            assertEquals("비밀번호를 확인해주세요", exception.getMessage());
        }

        @Test
        @DisplayName("회원탈퇴 - 실패:찾을수 없는 id")
        void fail_secession_without_id(){
            //given - email, password
            SignupRequestDto signupRequestDto = TestInfo.getOneSignUpRequestDto();
            String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());


            existingMember = new Member(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getUsername(),
                    encodedPassword, // 인코딩된 비밀번호 사용
                    UserRole.OWNER
            );
            existingMember.setId(testMemberId1);

            given(memberRepository.findById(testMemberId1)).willReturn(Optional.empty());

            //authMember
            AuthMember authMember = new AuthMember(
                    existingMember.getId(),
                    existingMember.getUsername(),
                    existingMember.getUserRole(),
                    existingMember.isActive(),
                    existingMember.isSecession()
            );

            //when
            NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                memberService.secession(authMember, secessionRequestDto);
            });

            assertEquals("잘못된 정보입니다.", exception.getMessage());
        }
    }
}
