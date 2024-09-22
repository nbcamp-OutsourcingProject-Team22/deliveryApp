package com.sparta.deliveryapp.domain.member.dto.response;

import com.sparta.deliveryapp.domain.member.UserRole;
import lombok.Getter;

@Getter
public class AuthInfoResponseDto {
    private final Integer id;
    private final String username;
    private final UserRole role;


    public AuthInfoResponseDto(Integer id, String username, UserRole role){
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
