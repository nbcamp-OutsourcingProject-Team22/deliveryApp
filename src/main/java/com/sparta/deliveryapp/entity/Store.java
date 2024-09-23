package com.sparta.deliveryapp.entity;


import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import com.sparta.deliveryapp.domain.store.validator.StoreValid;
import com.sparta.deliveryapp.exception.HandleNotFound;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
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
    private Boolean isClose;

    public static Store of(StoreRequestDto requestDto) {
        return new Store(
                null,
                requestDto.getStoreName(),
                requestDto.getOpeningTime(),
                requestDto.getClosingTime(),
                requestDto.getMinOrderAmount(),
                false,
                null
        );
    }

    public void closed() {
        this.isClose = true;
    }

    /**
     * 폐업 여부 확인
     * @throws HandleNotFound 가게가 폐업 상태일때 발생되는 예외
     */
    public void isClosed() {
        StoreValid.isCloseStore(this);
    }

    /**
     *  멤버와 1:N 관계
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "members_id")
    private Member member;

    /**
     *  멤버 연관관게 설정
     */
    public void addMember(Member member) {
        this.member = member;
    }

}
