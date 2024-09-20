package com.sparta.deliveryapp.domain.store;

import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
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

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    StoreRequestDto createStoreDto;
    StoreRequestDto updateStoreDto;

    @Mock
    private StoreRepository storeRepository;

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
            assertEquals(expectedMessage,actualMessage);
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
}