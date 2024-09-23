package com.sparta.deliveryapp.init;

import com.sparta.deliveryapp.domain.member.UserRole;
import com.sparta.deliveryapp.domain.member.dto.request.SignupRequestDto;
import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;

public class TestInfo {
    public static StoreRequestDto getOneStoreRequestDto() {
        return new StoreRequestDto(
                TestStoreInfo.TEST_STORE_ONE.getStoreName(),
                TestStoreInfo.TEST_STORE_ONE.getOpeningTime(),
                TestStoreInfo.TEST_STORE_ONE.getClosingTime(),
                TestStoreInfo.TEST_STORE_ONE.getMinOrderMount()
        );
    }
    public static StoreRequestDto getTwoStoreRequestDto() {
        return new StoreRequestDto(
                TestStoreInfo.TEST_STORE_TWO.getStoreName(),
                TestStoreInfo.TEST_STORE_TWO.getOpeningTime(),
                TestStoreInfo.TEST_STORE_TWO.getClosingTime(),
                TestStoreInfo.TEST_STORE_TWO.getMinOrderMount()
        );
    }

    public static StoreRequestDto getThreeStoreRequestDto() {
        return new StoreRequestDto(
                TestStoreInfo.TEST_STORE_THREE.getStoreName(),
                TestStoreInfo.TEST_STORE_THREE.getOpeningTime(),
                TestStoreInfo.TEST_STORE_THREE.getClosingTime(),
                TestStoreInfo.TEST_STORE_THREE.getMinOrderMount()
        );
    }
    public static Store getOneStore() {
        return new Store(
                null,
                TestStoreInfo.TEST_STORE_ONE.getStoreName(),
                TestStoreInfo.TEST_STORE_ONE.getOpeningTime(),
                TestStoreInfo.TEST_STORE_ONE.getClosingTime(),
                TestStoreInfo.TEST_STORE_ONE.getMinOrderMount(),
                false,
                null
        );
    }
    public static Store getTwoStore() {
        return new Store(
                null,
                TestStoreInfo.TEST_STORE_TWO.getStoreName(),
                TestStoreInfo.TEST_STORE_TWO.getOpeningTime(),
                TestStoreInfo.TEST_STORE_TWO.getClosingTime(),
                TestStoreInfo.TEST_STORE_TWO.getMinOrderMount(),
                false,
                null
        );
    }

    public static Store getThreeStore() {
        return new Store(
                null,
                TestStoreInfo.TEST_STORE_THREE.getStoreName(),
                TestStoreInfo.TEST_STORE_THREE.getOpeningTime(),
                TestStoreInfo.TEST_STORE_THREE.getClosingTime(),
                TestStoreInfo.TEST_STORE_THREE.getMinOrderMount(),
                false,
                null
        );
    }
    public static MenuRequest getOneMenuRequest() {
        return new MenuRequest(
                TestMenuInfo.TEST_MENU_ONE.getMenuName(),
                TestMenuInfo.TEST_MENU_ONE.getMenuPrice(),
                TestMenuInfo.TEST_MENU_ONE.getMenuDescription()
        );
    }
    public static MenuRequest getTwoMenuRequest() {
        return new MenuRequest(
                TestMenuInfo.TEST_MENU_TWO.getMenuName(),
                TestMenuInfo.TEST_MENU_TWO.getMenuPrice(),
                TestMenuInfo.TEST_MENU_TWO.getMenuDescription()
        );
    }
    public static MenuRequest getThreeMenuRequest() {
        return new MenuRequest(
                TestMenuInfo.TEST_MENU_THREE.getMenuName(),
                TestMenuInfo.TEST_MENU_THREE.getMenuPrice(),
                TestMenuInfo.TEST_MENU_THREE.getMenuDescription()
        );
    }
    public static Menu getOneMenu() {
        return new Menu(
                null,
                TestMenuInfo.TEST_MENU_ONE.getMenuName(),
                TestMenuInfo.TEST_MENU_ONE.getMenuPrice(),
                TestMenuInfo.TEST_MENU_ONE.getMenuDescription(),
                null,
                false
        );
    }
    public static Menu getTwoMenu() {
        return new Menu(
                null,
                TestMenuInfo.TEST_MENU_TWO.getMenuName(),
                TestMenuInfo.TEST_MENU_TWO.getMenuPrice(),
                TestMenuInfo.TEST_MENU_TWO.getMenuDescription(),
                null,
                false
        );
    }
    public static Menu getThreeMenu() {
        return new Menu(
                null,
                TestMenuInfo.TEST_MENU_THREE.getMenuName(),
                TestMenuInfo.TEST_MENU_THREE.getMenuPrice(),
                TestMenuInfo.TEST_MENU_THREE.getMenuDescription(),
                null,
                false
        );
    }

    public static SignupRequestDto getOneSignUpRequestDto() {
        return new SignupRequestDto(
                TestMemberInfo.TEST_MEMBER_ONE.getEmail(),
                TestMemberInfo.TEST_MEMBER_ONE.getUesrname(),
                TestMemberInfo.TEST_MEMBER_ONE.getPassword()
        );
    }
    public static SignupRequestDto getTwoSignUpRequestDto() {
        return new SignupRequestDto(
                TestMemberInfo.TEST_MEMBER_ONE.getEmail(),
                TestMemberInfo.TEST_MEMBER_ONE.getUesrname(),
                TestMemberInfo.TEST_MEMBER_ONE.getPassword()
        );
    }
    public static SignupRequestDto getThreeSignUpRequestDto() {
        return new SignupRequestDto(
                TestMemberInfo.TEST_MEMBER_ONE.getEmail(),
                TestMemberInfo.TEST_MEMBER_ONE.getUesrname(),
                TestMemberInfo.TEST_MEMBER_ONE.getPassword()
        );
    }

    public static Member getOwnerOneMember() {
        return new Member(
                TestMemberInfo.TEST_MEMBER_ONE.getEmail(),
                TestMemberInfo.TEST_MEMBER_ONE.getUesrname(),
                TestMemberInfo.TEST_MEMBER_ONE.getPassword(),
                UserRole.OWNER
        );
    }

    public static Member getOwnerTwoMember() {
        return new Member(
                TestMemberInfo.TEST_MEMBER_TWO.getEmail(),
                TestMemberInfo.TEST_MEMBER_TWO.getUesrname(),
                TestMemberInfo.TEST_MEMBER_TWO.getPassword(),
                UserRole.OWNER
        );
    }

    public static Member getOwnerThreeMember() {
        return new Member(
                TestMemberInfo.TEST_MEMBER_THREE.getEmail(),
                TestMemberInfo.TEST_MEMBER_THREE.getUesrname(),
                TestMemberInfo.TEST_MEMBER_THREE.getPassword(),
                UserRole.OWNER
        );
    }

public static Member getUserOneMember() {
    return new Member(
            TestMemberInfo.TEST_MEMBER_ONE.getEmail(),
            TestMemberInfo.TEST_MEMBER_ONE.getUesrname(),
            TestMemberInfo.TEST_MEMBER_ONE.getPassword(),
            UserRole.USER
    );
}

    public static Member getUserTwoMember() {
        return new Member(
                TestMemberInfo.TEST_MEMBER_TWO.getEmail(),
                TestMemberInfo.TEST_MEMBER_TWO.getUesrname(),
                TestMemberInfo.TEST_MEMBER_TWO.getPassword(),
                UserRole.USER
        );
    }

    public static Member getUserThreeMember() {
        return new Member(
                TestMemberInfo.TEST_MEMBER_THREE.getEmail(),
                TestMemberInfo.TEST_MEMBER_THREE.getUesrname(),
                TestMemberInfo.TEST_MEMBER_THREE.getPassword(),
                UserRole.USER
        );
    }
}
