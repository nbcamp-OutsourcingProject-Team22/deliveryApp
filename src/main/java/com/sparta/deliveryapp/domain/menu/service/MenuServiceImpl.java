package com.sparta.deliveryapp.domain.menu.service;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMemberEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseMenuEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseStoreEnum;
import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.menu.dto.MenuResponse;
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
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    // 메뉴 생성
    @Transactional
    public ApiResponse<Void> createMenu (AuthMember authMember, Long storeId, MenuRequest menuRequest) {

        Member member = validateMember(authMember);

        Store store = validateStore(storeId, member);

        Menu menu = new Menu(
                null,
                menuRequest.getMenuName(),
                menuRequest.getMenuPrice(),
                menuRequest.getMenuDescription(),
                store,
                false
        );

        menuRepository.save(menu);
        return new ApiResponse<>(ApiResponseMenuEnum.MENU_SAVE_SUCCESS);
    }

    // 메뉴 수정
    @Transactional
    public ApiResponse<MenuResponse> updateMenu(AuthMember authMember, Long storeId, Long menuId, MenuRequest menuRequest){

        Member member = validateMember(authMember);

        validateStore(storeId, member);

        Menu menu = validateMenu(menuId, storeId);

        menu.update(menuRequest.getMenuName(), menuRequest.getMenuPrice(), menuRequest.getMenuDescription());
        Menu savedMenu = menuRepository.save(menu);
        return new ApiResponse<> (ApiResponseMenuEnum.MENU_UPDATE_SUCCESS, new MenuResponse(savedMenu));
    }

    // 메뉴 삭제
    @Transactional
    public ApiResponse<Void> deleteMenu(AuthMember authMember, Long storeId, Long menuId) {

        Member member = validateMember(authMember);

        validateStore(storeId, member);

        Menu menu = validateMenu(menuId, storeId);

        // 메뉴 삭제 상태로 변경
        menu.markAsDeleted();
        menuRepository.save(menu);

        return new ApiResponse<>(ApiResponseMenuEnum.MENU_DELETE_SUCCESS);
    }

    // 멤버 확인 메서드
    public Member validateMember (AuthMember authMember){
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new HandleNotFound(ApiResponseMemberEnum.MEMBER_NOT_FOUND));
        if (member.getUserRole() != UserRole.OWNER) {
            throw new HandleUnauthorizedException(ApiResponseMenuEnum.NOT_OWNER);
        }
        return member;
    }

    // 가게 확인 메서드
    public Store validateStore (Long storeId, Member member){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new HandleNotFound(ApiResponseStoreEnum.STORE_NOT_FOUND));
        if (!Objects.equals(store.getMember().getId(), member.getId())){
            throw new HandleUnauthorizedException(ApiResponseMenuEnum.NOT_OWNER);
        }
        return store;
    }

    // 메뉴 확인 메서드
    public Menu validateMenu (Long menuId, Long storeId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new HandleNotFound(ApiResponseMenuEnum.MENU_NOT_FOUND));
        if (!Objects.equals(menu.getStore().getId(), storeId)){
            throw new HandleUnauthorizedException(ApiResponseMenuEnum.NOT_OWNER);
        }
        return menu;
    }

    public Menu validateMenuTest (Long menuId, Long storeId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new HandleNotFound(ApiResponseMenuEnum.MENU_NOT_FOUND));
        return menu.validateMenu(storeId);
    }


}
