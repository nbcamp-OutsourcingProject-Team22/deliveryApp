package com.sparta.deliveryapp.entity;


import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Store extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Column(nullable = false)
    private Integer minOrderAmount;

    @Column(nullable = false)
    private boolean isClose;

    public static Store of(StoreRequestDto requestDto) {
        return new Store(
                null,
                requestDto.getStoreName(),
                requestDto.getOpeningTime(),
                requestDto.getClosingTime(),
                requestDto.getMinOrderAmount(),
                false
        );
    }

    /**
     *  멤버와 1:N 관계
     */
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Member member;

    /**
     *  멤버 연관관게 설정
     */
//    public void addMember(Member member) {
//        this.member = member;
//    }

}
