package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.member.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="member")
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ColumnDefault("true")
    private boolean isActive = true; //활동계정 default true

    @ColumnDefault("false")
    private boolean isSecession = false; //회원탈퇴 default false


    public Member(
            @NotBlank @Email String email,
            @NotBlank String username,
            String encodedPassword,
            UserRole userRole
            ){
        this.email = email;
        this.username = username;
        this.password = encodedPassword;
        this.userRole = userRole;
    }
}
