package com.sparta.deliveryapp.domain.review.dto;

import com.sparta.deliveryapp.apiResponseEnum.ApiResponseReviewEnum;
import com.sparta.deliveryapp.entity.Review;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import lombok.Getter;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Getter
public class ReviewRatingResponseDto {

    private final int rating;
    private final String contents;
    private final Store storeId;


    public ReviewRatingResponseDto(int rating, String contents, Store storeId) {
        if (storeId == null){
            throw new HandleNotFound(ApiResponseReviewEnum.STORE_NOT_FOUND);
        }
        this.rating = rating;
        this.contents = contents;
        this.storeId = storeId;
    }

    public static ReviewRatingResponseDto from(Review review) {
        return new ReviewRatingResponseDto(review.getRating(), review.getContents(),review.getStore());
    }
}
