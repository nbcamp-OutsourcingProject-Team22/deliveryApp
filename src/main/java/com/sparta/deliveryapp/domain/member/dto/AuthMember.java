package com.sparta.deliveryapp.domain.member.dto;

import com.sparta.deliveryapp.domain.member.UserRole;
import lombok.Getter;


@Getter
public class AuthMember {
    private final Long id;
    private final String username;
    private final UserRole role;

    public AuthMember(Long id, String username, UserRole role){
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
