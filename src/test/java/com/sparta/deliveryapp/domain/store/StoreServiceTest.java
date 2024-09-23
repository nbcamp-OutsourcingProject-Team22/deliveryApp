package com.sparta.deliveryapp.domain.store;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.member.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.model.StoreResponseDto;
import com.sparta.deliveryapp.domain.store.model.StoresResponseDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.domain.store.service.StoreServiceImpl;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleMaxException;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import com.sparta.deliveryapp.init.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    // 가게 생성, 업데이트 데이터
    StoreRequestDto createStoreDto; StoreRequestDto updateStoreDto;
    // 가게 Entity 생성, 업데이트 데이터
    Store testStore; Store testUpdateStore;
    // 메뉴 생성 데이터
    MenuRequest menuRequest;
    // 회원가입 데이터
    SignupRequestDto signupRequestDto; SignupRequestDto ohterSignupRequestDto;
    // 유저 아이디 데이터
    Long testMemberId1 = 1L; Long testMemberId2 = 2L;
    // 가게 아이디 데이터
    Long testStoreId = 1L;
    // 메뉴 아이디 데이터
    Long testMenuId = 1L;
    // 사장,다른사장,일반유저 Entity 데이터
    Member ownerMember; Member otherOwnerMember; Member userMember;
    // 메뉴 Entity 데이터
    Menu menu;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @BeforeEach
    void setUp() {
        // Store
        createStoreDto = TestInfo.getOneStoreRequestDto();
        testStore = TestInfo.getOneStore();
        ReflectionTestUtils.setField(testStore, "id", testStoreId);
        ReflectionTestUtils.setField(testStore,"member",ownerMember);

        updateStoreDto = TestInfo.getTwoStoreRequestDto();
        testUpdateStore = TestInfo.getTwoStore();
        ReflectionTestUtils.setField(testUpdateStore, "id", testStoreId);
        ReflectionTestUtils.setField(testUpdateStore,"member",ownerMember);

        // Menu
        menuRequest = TestInfo.getOneMenuRequest();
        menu = TestInfo.getOneMenu();
        ReflectionTestUtils.setField(menu, "id", testMenuId);
        ReflectionTestUtils.setField(menu,"store",testStore);

        // Member - Owner
        signupRequestDto = TestInfo.getOneSignUpRequestDto();
        ownerMember = TestInfo.getOwnerOneMember();
        ReflectionTestUtils.setField(ownerMember, "id", testMemberId1);

        ohterSignupRequestDto = TestInfo.getTwoSignUpRequestDto();
        otherOwnerMember = TestInfo.getOwnerTwoMember();
        ReflectionTestUtils.setField(otherOwnerMember, "id", testMemberId2);

        // Member - User
        userMember = TestInfo.getUserOneMember();
        ReflectionTestUtils.setField(userMember, "id", testMemberId1);

    }

    @Nested
    public class 가게_생성_테스트 {
        @Test
        @DisplayName("가게 생성 성공")
        void test1() {
            // given - 성공하였을때 메세지 준비, 연관관계 설정 멤버 추가
            List<Store> stores = List.of(testStore);
            String expectedMessage = "가게 저장에 성공 하였습니다";
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(ownerMember));
            given(storeRepository.findAllByMemberAndIsCloseFalse(ownerMember)).willReturn(stores);

            // when - 가게 생성 시도
            String actualMessage = storeService.createStore(testMemberId1, createStoreDto).getMessage();

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedMessage,
                    actualMessage
            );
        }

        @Test
        @DisplayName("가게 생성 실패 _ 유저 못 찾음")
        void test2() {
            // given - 성공하였을때 메세지 준비, 연관관계 설정 멤버 추가
            String expectedExceptionMessage = "멤버를 찾을 수 없습니다.";
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.empty());

            // when - 가게 생성 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.createStore(testMemberId1, createStoreDto));

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 생성 실패 _ 사장 권한 아님")
        void test3() {
            // given - 성공하였을때 메세지 준비, 연관관계 설정 멤버 추가
            String expectedExceptionMessage = "사장 권한이 아닙니다";
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(userMember));

            HandleUnauthorizedException actualException = assertThrows(HandleUnauthorizedException.class, () ->
                    storeService.createStore(testMemberId1, createStoreDto));

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 생성 실패 _ 가게 3개 까지 가능")
        void test4() {
            // given - 성공하였을때 메세지 준비, 연관관계 설정 멤버 추가
            List<Store> stores = List.of(testStore,testStore,testStore,testStore);
            String expectedExceptionMessage = "가게는 3개까지 만들 수 있습니다";
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(ownerMember));
            given(storeRepository.findAllByMemberAndIsCloseFalse(ownerMember)).willReturn(stores);

            // when - 가게 생성 시도
            HandleMaxException actualException = assertThrows(HandleMaxException.class, () ->
                    storeService.createStore(testMemberId1, createStoreDto));

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }
    }

    @Nested
    public class 가게_수정_테스트 {
        @Test
        @DisplayName("가게 수정 성공")
        void test1() {
            // given - 성공하였을때, 메시지 준비
            String expectedMessage = "가게 업데이트에 성공 하였습니다";

            given(storeRepository.findById(testStoreId)).willReturn(Optional.of(testStore));
            testStore.addMember(ownerMember);
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(ownerMember));


            // when - 가게 수정 시도
            String actualMessage = storeService.updateStore(testMemberId1, testStoreId, updateStoreDto).getMessage();

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedMessage,
                    actualMessage
            );
        }

        @Test
        @DisplayName("가게 수정 실패 _ 가게 못찾음")
        void test2() {
            // given - 실패하였을때, 메시지 준비
            String expectedExceptionMessage = "가게를 찾을 수 없습니다";
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(ownerMember));
            given(storeRepository.findById(testStoreId)).willReturn(Optional.empty());

            // when - 가게 수정 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.updateStore(testMemberId1, testStoreId, updateStoreDto)
            );

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 수정 실패 _ 유저 못찾음")
        void test3() {
            // given - 실패하였을때, 메시지 준비
            String expectedExceptionMessage = "멤버를 찾을 수 없습니다.";

            given(memberRepository.findById(testMemberId1)).willReturn(Optional.empty());

            // when - 가게 수정 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.updateStore(testMemberId1, testStoreId, updateStoreDto)
            );

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 수정 실패 _ 사장 권한이 아님")
        void test4() {
            // given - 실패하였을때, 메시지 준비
            String expectedExceptionMessage = "사장 권한이 아닙니다";

            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(userMember));

            // when - 가게 수정 시도
            HandleUnauthorizedException actualException = assertThrows(HandleUnauthorizedException.class, () ->
                    storeService.updateStore(testMemberId1, testStoreId, updateStoreDto)
            );

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 수정 실패 _ 본인이 만든 가게가 아님")
        void test5() {
            // given - 성공하였을때, 메시지 준비
            String expectedExceptionMessage = "가게 주인이 아닙니다";

            given(storeRepository.findById(testStoreId)).willReturn(Optional.of(testUpdateStore));
            testUpdateStore.addMember(ownerMember);
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(otherOwnerMember));

            // when - 가게 수정 시도
            HandleUnauthorizedException actualException = assertThrows(HandleUnauthorizedException.class, () ->
                    storeService.updateStore(testMemberId1, testStoreId, updateStoreDto)
            );

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }
    }

    @Nested
    public class 가게_단건_조회_테스트 {
        @Test
        @DisplayName("가게 단건 조회 성공")
        void test1() {
            // given - 가게 조회 생성 준비 상황
            given(storeRepository.findByStoreName(testStore.getStoreName())).willReturn(Optional.of(testStore));
            given(menuRepository.findByStore(testStore)).willReturn(Optional.of(menu));

            // when - 가게 조회 시도
            StoreResponseDto actualData = storeService.getStore(testStore.getStoreName()).getData();

            //then - 예상한 store 아이디와, 데이터로 넘오온 store 이름이 일치하는지 확인
            assertEquals(
                    testStore.getStoreName(),
                    actualData.getStoreName()
            );
        }

        @Test
        @DisplayName("가게 단건 조회 실패 _ 가게 조회되지않음")
        void test2() {
            // given - 가게 조회 실패 준비 상황
            String storeName = createStoreDto.getStoreName();
            String expectedExceptionMessage = "가게를 찾을 수 없습니다";
            given(storeRepository.findByStoreName(storeName)).willReturn(Optional.empty());

            // when - 가게 조회 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.getStore(storeName)
            );

            //then - 예상한 예외 메시지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 단건 조회 실패 _ 가게 폐업")
        void test3() {
            // given - 가게 조회 생성 준비 상황, 가게 폐업 실행
            testStore.closed();
            String storeName = createStoreDto.getStoreName();
            String expectedExceptionMessage = "해당 가게는 폐업 하였습니다";
            given(storeRepository.findByStoreName(storeName)).willReturn(Optional.of(testStore));

            // when - 가게 조회 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.getStore(storeName)
            );

            //then - 예상한 예외 메시지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }
        @Test
        @DisplayName("가게 단건 조회 성공 _ 메뉴 없음")
        void test4() {
            // given - 가게 조회 생성 준비 상황
            given(storeRepository.findByStoreName(testStore.getStoreName())).willReturn(Optional.of(testStore));
            given(menuRepository.findByStore(testStore)).willReturn(Optional.empty());

            // when - 가게 조회 시도
            StoreResponseDto actualData = storeService.getStore(testStore.getStoreName()).getData();

            //then - 예상한 store 아이디와, 데이터로 넘오온 store 이름이 일치하는지 확인
            assertEquals(
                    testStore.getStoreName(),
                    actualData.getStoreName()
            );
        }
    }

    @Nested
    public class 가게_다건_조회_테스트 {
        @Test
        @DisplayName("가게 다건 조회 성공 _ 가게 이름 공백")
        void test1() {
            // given - 가게 이름 없을때 다건 조회, 가게 준비, 페이지 정보 준비
            String expectedMessage = "가게 조회에 성공 하였습니다";
            List<Store> stores = List.of(testStore);
            String storeName = "";
            int page = 1;
            int size = 10;
            String sort = "asc";
            Sort.Direction direction = Sort.Direction.fromString(sort);
            Pageable pageable = PageRequest.of(page, size, direction, "createdAt");
            Page<Store> storePage = new PageImpl<>(stores, pageable, stores.size());
            given(storeRepository.findAllByIsCloseFalse(pageable)).willReturn(storePage);

            // when - 가게 다건 조회 시도
            ApiResponse<List<StoresResponseDto>> actualData = storeService.getStores(storeName, pageable);

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedMessage,
                    actualData.getMessage()
            );

        }

        @Test
        @DisplayName("가게 다건 조회 성공 _ 가게 이름 있음")
        void test2() {
            // given - 가게 이름 있을때 다건 조회, 가게 준비, 페이지 정보 준비
            String expectedMessage = "가게 조회에 성공 하였습니다";
            List<Store> stores = List.of(testStore);
            String storeName = "test";
            int page = 1;
            int size = 10;
            String sort = "asc";
            Sort.Direction direction = Sort.Direction.fromString(sort);
            Pageable pageable = PageRequest.of(page, size, direction, "createdAt");
            Page<Store> storePage = new PageImpl<>(stores, pageable, stores.size());
            given(storeRepository.findAllByStoreNameContainingAndIsCloseFalse(storeName, pageable)).willReturn(storePage);

            // when - 가게 다건 조회 시도
            ApiResponse<List<StoresResponseDto>> actualData = storeService.getStores(storeName, pageable);

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedMessage,
                    actualData.getMessage()
            );
        }
    }

    @Nested
    public class 가게_폐업_테스트 {
        @Test
        @DisplayName("가게 폐업 성공")
        void test1() {
            // given - 조회에 성공할 가게 준비, 폐업할 가게 준비
            String expectedMessage = "가게 폐업에 성공 하였습니다";
            given(storeRepository.findById(testStoreId)).willReturn(Optional.of(testStore));
            testStore.addMember(ownerMember);
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(ownerMember));


            // when - 가게 폐업 시도
            String actualMessage = storeService.closeStore(testMemberId1,testStoreId).getMessage();

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedMessage,
                    actualMessage
            );
        }

        @Test
        @DisplayName("가게 폐업 실패 _ 가게 못찾음")
        void test2() {
            // given - 조회에 성공할 가게 준비, 폐업할 가게 준비
            String expectedExceptionMessage = "가게를 찾을 수 없습니다";
            given(storeRepository.findById(testStoreId)).willReturn(Optional.empty());

            // when - 가게 폐업 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.closeStore(testMemberId1,testStoreId)
            );

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }
        @Test
        @DisplayName("가게 폐업 실패 _ 유저 못찾음")
        void test3() {
            // given - 조회에 성공할 가게 준비, 폐업할 가게 준비
            String expectedExceptionMessage = "멤버를 찾을 수 없습니다.";
            given(storeRepository.findById(testStoreId)).willReturn(Optional.of(testStore));
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.empty());

            // when - 가게 폐업 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.closeStore(testMemberId1,testStoreId)
            );

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 폐업 실패 _ 사장 권한 아님")
        void test4() {
            // given - 조회에 성공할 가게 준비, 폐업할 가게 준비
            String expectedExceptionMessage = "사장 권한이 아닙니다";
            given(storeRepository.findById(testStoreId)).willReturn(Optional.of(testStore));
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(userMember));

            // when - 가게 폐업 시도
            HandleUnauthorizedException actualException = assertThrows(HandleUnauthorizedException.class, () ->
                    storeService.closeStore(testMemberId1,testStoreId)
            );

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }

        @Test
        @DisplayName("가게 폐업 실패 _ 본인이 만든 가게가 아님")
        void test5() {
            // given - 조회에 성공할 가게 준비, 폐업할 가게 준비
            String expectedExceptionMessage = "가게 주인이 아닙니다";
            given(storeRepository.findById(testStoreId)).willReturn(Optional.of(testStore));
            testStore.addMember(ownerMember);
            given(memberRepository.findById(testMemberId1)).willReturn(Optional.of(otherOwnerMember));

            // when - 가게 폐업 시도
            HandleUnauthorizedException actualException = assertThrows(HandleUnauthorizedException.class, () ->
                    storeService.closeStore(testMemberId1,testStoreId)
            );

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }
    }
}
