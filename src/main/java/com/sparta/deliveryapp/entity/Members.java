package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.member.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="members")
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public Members(@NotBlank @Email String email, @NotBlank String username, String encodedPassword, UserRole userRole) {
        this.email = email;
        this.username = username;
        this.password = encodedPassword;
        this.userRole = userRole;
    }
}
