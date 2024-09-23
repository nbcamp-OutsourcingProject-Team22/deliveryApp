package com.sparta.deliveryapp.init;

public enum TestMemberInfo {
    TEST_MEMBER_ONE(
            "test1@naver.com",
            "test1",
            "!@Skdud340"
    ),
    TEST_MEMBER_TWO(
            "test1@naver.com",
            "test1",
            "!@Skdud340"
    ),
    TEST_MEMBER_THREE(
            "test1@naver.com",
            "test1",
            "!@Skdud340"
    ),
    ;

    private final String email;
    private final String uesrname;
    private final String password;

    TestMemberInfo(String email, String uesrname, String password) {
        this.email = email;
        this.uesrname = uesrname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUesrname() {
        return uesrname;
    }

    public String getPassword() {
        return password;
    }
}
