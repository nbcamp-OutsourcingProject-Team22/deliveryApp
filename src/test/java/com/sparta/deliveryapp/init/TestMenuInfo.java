package com.sparta.deliveryapp.init;

public enum TestMenuInfo {
    TEST_MENU_ONE(
            "testMenu1",
            5000,
            "testDescription1"
    ),
    TEST_MENU_TWO(
            "testMenu2",
            5000,
            "testDescription2"
    ),
    TEST_MENU_THREE(
            "testMenu3",
            5000,
            "testDescription3"
    ),
    ;

    private final String menuName;
    private final Integer menuPrice;
    private final String menuDescription;

    TestMenuInfo(String menuName, Integer menuPrice, String menuDescription) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
    }

    public String getMenuName() {
        return menuName;
    }

    public Integer getMenuPrice() {
        return menuPrice;
    }

    public String getMenuDescription() {
        return menuDescription;
    }
}
