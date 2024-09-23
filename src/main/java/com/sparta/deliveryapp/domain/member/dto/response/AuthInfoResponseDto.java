package com.sparta.deliveryapp.domain.member.dto.response;

import com.sparta.deliveryapp.domain.member.UserRole;
import lombok.Getter;

@Getter
public class AuthInfoResponseDto {
    private final Integer id;
    private final String username;
    private final UserRole role;
    private final boolean isActive;
    private final boolean isSecession;


    public AuthInfoResponseDto(Integer id, String username, UserRole role, Boolean isActive, Boolean isSecession){
        this.id = id;
        this.username = username;
        this.role = role;
        this.isActive = isActive;
        this.isSecession = isSecession;
    }


}
