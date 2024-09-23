package com.sparta.deliveryapp.domain.menu.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMemberEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMenuEnum;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.repository.MenuRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    // 메뉴 생성
    @Transactional
    public ApiResponse<Void> createMenu (AuthMember authMember, Long storeId, MenuRequest menuRequest) {
        // TODO: 30번째 줄 예외처리 바꿔야 함 PASSWORD_UNAUTHORIZED 에서 -> MEMEBER_NOT_FOUND 로 만들어 줘야함
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(()-> new HandleNotFound(ApiResponseMemberEnum.PASSWORD_UNAUTHORIZED));
        if (member.getUserRole() != UserRole.OWNER){
            throw new HandleUnauthorizedException(ApiResponseMenuEnum.NOT_OWNER);
        }

        Store store = storeRepository.findById(storeId).orElseThrow(()-> new HandleNotFound(ApiResponseMenuEnum.NOT_OWNER));
        if (!Objects.equals(store.getMember().getId(), member.getId())){ // 매개변수 1번(46번째 store instance의 멤버아이디)과 2번(40번째줄)이 같은지 확인
            throw new HandleUnauthorizedException(ApiResponseMenuEnum.NOT_OWNER);
        }

        Menu menu = new Menu( // create를 먼저 해야 비교가능한데 연관관계가 설정되기 이전이라 인증2는 현재 create 에서 불가
                null,
                menuRequest.getMenuName(),
                menuRequest.getMenuPrice(),
                menuRequest.getMenuDescription(),
                store
        );

        menuRepository.save(menu);
        return new ApiResponse<>(ApiResponseMenuEnum.MENU_SAVE_SUCCESS);
    }
}
