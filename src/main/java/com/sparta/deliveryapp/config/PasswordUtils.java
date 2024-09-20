package com.sparta.deliveryapp.config;

public class PasswordUtils {
    // 비밀번호가 요구 사항을 충족하는지 확인합니다
    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;  // 비밀번호 길이 체크
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }

            // 모든 조건이 만족되면 반복 종료
            if (hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar) {
                return true;
            }
        }

        return false;  // 모든 조건이 충족되지 않았을 경우 false 반환
    }
}