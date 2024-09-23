package com.sparta.deliveryapp.domain.review.dto;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseReviewEnum;
import com.sparta.deliveryapp.entity.Review;
import com.sparta.deliveryapp.exception.HandleNotFound;
import lombok.Getter;

@Getter
public class ReviewRatingResponseDto {

    private final String storeName;
    private final int rating;
    private final String contents;

    public ReviewRatingResponseDto(String storeName,int rating, String contents) {
        if (storeName == null){
            throw new HandleNotFound(ApiResponseReviewEnum.STORE_NOT_FOUND);
        }
        this.storeName = storeName;
        this.rating = rating;
        this.contents = contents;
    }

    public static ReviewRatingResponseDto from(Review review) {
        return new ReviewRatingResponseDto(review.getStore().getStoreName(),review.getRating(), review.getContents());
    }
}
