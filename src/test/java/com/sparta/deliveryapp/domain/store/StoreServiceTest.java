package com.sparta.deliveryapp.domain.store;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.model.StoreResponseDto;
import com.sparta.deliveryapp.domain.store.model.StoresResponseDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.domain.store.service.StoreService;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    StoreRequestDto createStoreDto;
    StoreRequestDto updateStoreDto;
    MenuRequest menuRequest;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        createStoreDto = new StoreRequestDto(
                "testStore",
                LocalTime.of(9, 0, 0),
                LocalTime.of(18, 0, 0),
                7000
        );
        updateStoreDto = new StoreRequestDto(
                "testStore111",
                LocalTime.of(11,0,0),
                LocalTime.of(20,0,0),
                4500
        );
        menuRequest = new MenuRequest(
                "testName",
                7000,
                "testDes"
        );
    }

    @Nested
    public class 가게_생성_테스트 {
        @Test
        @DisplayName("가게 생성 성공")
        void test1() {
            // given - 성공하였을때, 메시지 준비
            String expectedMessage = "가게 저장에 성공 하였습니다";

            // when - 가게 생성 시도
            String actualMessage = storeService.createStore(createStoreDto).getMessage();

            //then - 예상한 메시지와, 실제 메세지가 일치하는지 확인
            assertEquals(
                    expectedMessage,
                    actualMessage
            );
        }
    }

    @Nested
    public class 가게_수정_테스트 {
        @Test
        @DisplayName("가게 수정 성공")
        void test1() {
            // given - 성공하였을때, 메시지 준비
            Long storeId = 1L;
            String expectedMessage = "가게 업데이트에 성공 하였습니다";
            Store expectedStore = Store.of(updateStoreDto);
            given(storeRepository.findById(storeId)).willReturn(Optional.of(expectedStore));

            // when - 가게 수정 시도
            String actualMessage = storeService.updateStore(storeId, updateStoreDto).getMessage();

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
            Long storeId = 1L;
            String expectedExceptionMessage = "가게를 찾을 수 없습니다";
            given(storeRepository.findById(storeId)).willReturn(Optional.empty());

            // when - 가게 수정 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.updateStore(storeId, updateStoreDto)
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
        @DisplayName("가게 조회 성공")
        void test1() {
            // given - 가게 조회 생성 준비 상황
            Store expectedStore = Store.of(createStoreDto);
            String storeName = expectedStore.getStoreName();
            Menu menu = new Menu(
                    null,
                    menuRequest.getMenuName(),
                    menuRequest.getMenuPrice(),
                    menuRequest.getMenuDescription(),
                    expectedStore
            );
            given(storeRepository.findByStoreName(storeName)).willReturn(Optional.of(expectedStore));
            given(menuRepository.findByStore(expectedStore)).willReturn(Optional.of(menu));

            // when - 가게 조회 시도
            StoreResponseDto actualData = storeService.getStore(storeName).getData();

            //then - 예상한 store 아이디와, 데이터로 넘오온 store 이름이 일치하는지 확인
            assertEquals(
                    expectedStore.getStoreName(),
                    actualData.getStoreName()
            );
        }

        @Test
        @DisplayName("가게 단건 조회 실패 _ 가게 조회되지않음")
        void test2() {
            // given - 가게 조회 생성 준비 상황
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
    }
    @Nested
    public class 가게_다건_조회_테스트 {
        @Test
        @DisplayName("가게 다건 조회 성공 _ 가게 이름 공백")
        void test1() {
            // given - 가게 이름 없을때 다건 조회, 가게 준비, 페이지 정보 준비
            String expectedMessage = "가게 조회에 성공 하였습니다";
            Store store = Store.of(createStoreDto);
            List<Store> stores = List.of(store);
            String storeName = "";
            int page = 1;
            int size = 10;
            String sort = "asc";
            Sort.Direction direction = Sort.Direction.fromString(sort);
            Pageable pageable = PageRequest.of(page,size,direction,"createdAt");
            Page<Store> storePage = new PageImpl<>(stores,pageable,stores.size());
            given(storeRepository.findAll(pageable)).willReturn(storePage);

            // when - 가게 다건 조회 시도
            ApiResponse<List<StoresResponseDto>> actualData = storeService.getStores(storeName,pageable);

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
            Store store = Store.of(createStoreDto);
            List<Store> stores = List.of(store);
            String storeName = "test";
            int page = 1;
            int size = 10;
            String sort = "asc";
            Sort.Direction direction = Sort.Direction.fromString(sort);
            Pageable pageable = PageRequest.of(page,size,direction,"createdAt");
            Page<Store> storePage = new PageImpl<>(stores,pageable,stores.size());
            given(storeRepository.findAllByStoreNameContaining(storeName,pageable)).willReturn(storePage);

            // when - 가게 다건 조회 시도
            ApiResponse<List<StoresResponseDto>> actualData = storeService.getStores(storeName,pageable);

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
            Long storeId = 1L;
            String expectedMessage = "가게 폐업에 성공 하였습니다";
            Store store = Store.of(createStoreDto);
            ReflectionTestUtils.setField(store,"id",storeId);
            given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

            // when - 가게 폐업 시도
            String actualMessage = storeService.closeStore(storeId).getMessage();

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
            Long storeId = 1L;
            String expectedExceptionMessage = "가게를 찾을 수 없습니다";
            given(storeRepository.findById(storeId)).willReturn(Optional.empty());

            // when - 가게 폐업 시도
            HandleNotFound actualException = assertThrows(HandleNotFound.class, () ->
                    storeService.closeStore(storeId)
            );

            // then - 예상한 메세지와 동일한지 확인
            assertEquals(
                    expectedExceptionMessage,
                    actualException.getApiResponseEnum().getMessage()
            );
        }
    }
}