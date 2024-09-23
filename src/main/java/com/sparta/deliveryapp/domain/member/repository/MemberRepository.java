package com.sparta.deliveryapp.domain.member.repository;

import com.sparta.deliveryapp.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(@NotBlank @Email String email);

    Optional<Member> findByUsername(@NotBlank String username);
}
