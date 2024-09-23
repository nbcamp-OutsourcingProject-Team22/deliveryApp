package com.sparta.deliveryapp.domain.review.dto;

import com.sparta.deliveryapp.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private final int rating;
    private final String contents;

    public ReviewResponseDto(int rating, String contents) {
        this.rating = rating;
        this.contents = contents;
    }

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(review.getRating(), review.getContents());
    }
}
