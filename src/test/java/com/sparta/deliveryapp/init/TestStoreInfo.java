package com.sparta.deliveryapp.init;

import java.time.LocalTime;

public enum TestStoreInfo {
    TEST_STORE_ONE(
            "testStore1",
            LocalTime.of(9, 0, 0),
            LocalTime.of(18, 0, 0),
            6000
            ),
    TEST_STORE_TWO(
            "testStore2",
            LocalTime.of(10, 0, 0),
            LocalTime.of(19, 0, 0),
            7000
    ),
    TEST_STORE_THREE(
            "testStore3",
            LocalTime.of(11, 0, 0),
            LocalTime.of(20, 0, 0),
            8000
    ),
    ;

    private final String storeName;
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final Integer minOrderMount;

    TestStoreInfo(String storeName, LocalTime openingTime, LocalTime closingTime, Integer minOrderMount) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minOrderMount = minOrderMount;
    }

    public String getStoreName() {
        return storeName;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public Integer getMinOrderMount() {
        return minOrderMount;
    }
}
