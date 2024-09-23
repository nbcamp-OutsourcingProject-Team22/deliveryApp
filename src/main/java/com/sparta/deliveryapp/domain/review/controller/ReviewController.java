package com.sparta.deliveryapp.domain.review.controller;


import com.sparta.deliveryapp.annotation.Auth;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseReviewEnum;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.review.dto.ReviewRatingResponseDto;
import com.sparta.deliveryapp.domain.review.dto.ReviewRequestDto;
import com.sparta.deliveryapp.domain.review.dto.ReviewResponseDto;
import com.sparta.deliveryapp.domain.review.service.ReviewService;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewRatingResponseDto>> createReview
            (@RequestBody @Valid ReviewRequestDto requestDto,@Auth AuthMember authMember){
        ApiResponse<ReviewRatingResponseDto> createdReview = reviewService.createReview(requestDto,authMember);
        return ResponseEntity.ok(createdReview);
    }

    // 가게 별 리뷰 전체 조회
    @GetMapping("/store")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviewsByStore
            (@RequestParam("storeId") Long storeId){
        ApiResponse<List<ReviewResponseDto>> storeReviewList = reviewService.getReviewByStore(storeId);
        if (storeReviewList.getData().isEmpty()){
            throw new HandleUnauthorizedException(ApiResponseReviewEnum.EMPTY_STORE_REVIEW);
        }
        return ResponseEntity.ok(storeReviewList);
    }

    // 별점 범위 별 리뷰 조회
    @GetMapping("/rating")
    public ResponseEntity<ApiResponse<List<ReviewRatingResponseDto>>> getReviewsByRating
        (@RequestParam int minRating,@RequestParam int maxRating){

        ApiResponse<List<ReviewRatingResponseDto>> ratingReviewList = reviewService
                .getReviewsByRating(minRating,maxRating);
        if (ratingReviewList.getData().isEmpty()){
            throw new HandleUnauthorizedException(ApiResponseReviewEnum.EMPTY_RATING_REVIEW);
        }
        return ResponseEntity.ok(ratingReviewList);
    }

}
