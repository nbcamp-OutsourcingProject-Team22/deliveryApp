package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.review.dto.ReviewRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends Timestamped {

    private static ReviewRequestDto reviewRequestDto;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private int rating;

    @Column(nullable = false)
    private String contents;

    public Review(Member member,Store store,Order order,int rating, String contents) {
        this.member = member;
        this.store = store;
        this.order = order;
        this.rating = rating;
        this.contents = contents;

    }

    public static Review from(ReviewRequestDto reviewRequestDto,Member member,Store store,Order order) {
        return new Review(member,store,order,reviewRequestDto.getRating(),reviewRequestDto.getContents());
    }




}
