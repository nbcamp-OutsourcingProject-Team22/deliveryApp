package com.sparta.deliveryapp.domain.review.service;


import com.sparta.deliveryapp.apiResponseEnum.ApiResponse;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseOrderEnum;
import com.sparta.deliveryapp.apiResponseEnum.ApiResponseReviewEnum;
import com.sparta.deliveryapp.domain.member.dto.AuthMember;
import com.sparta.deliveryapp.domain.member.repository.MemberRepository;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.repository.OrderRepository;
import com.sparta.deliveryapp.domain.review.dto.ReviewRatingResponseDto;
import com.sparta.deliveryapp.domain.review.dto.ReviewRequestDto;
import com.sparta.deliveryapp.domain.review.dto.ReviewResponseDto;
import com.sparta.deliveryapp.domain.review.repository.ReviewRepository;
import com.sparta.deliveryapp.domain.store.repository.StoreRepository;
import com.sparta.deliveryapp.entity.Member;
import com.sparta.deliveryapp.entity.Order;
import com.sparta.deliveryapp.entity.Review;
import com.sparta.deliveryapp.entity.Store;
import com.sparta.deliveryapp.exception.HandleNotFound;
import com.sparta.deliveryapp.exception.HandleUnauthorizedException;
import com.sparta.deliveryapp.exception.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;


    // 리뷰 생성
    @Transactional
    public ApiResponse<ReviewRatingResponseDto> createReview(ReviewRequestDto requestDto, AuthMember authMember) { //, AuthMember authMember

        // 주문 찾기
        Order order = checkOrder(requestDto.getOrderId());

        // 멤버 찾기
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(()->new HandleNotFound(ApiResponseReviewEnum.MEMBER_NOT_FOUND));

        // 가게 찾기
        Store store = order.getStore();

        checkMemberOrder(order, member);

        // 리뷰 저장
        Review review = Review.from(requestDto,member,store,order);

        ReviewRatingResponseDto responseDto = ReviewRatingResponseDto.from(reviewRepository.save(review));
        return new ApiResponse<>(ApiResponseReviewEnum.REVIEW_SAVE_SUCCESS,responseDto);
    }

    // 가게 별 리뷰 조회
    public ApiResponse<List<ReviewResponseDto>> getReviewByStore(Long storeId) {
        // 스토어 ID 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseReviewEnum.STORE_NOT_FOUND));

        // ApiResponse 생성 및 반환
        return new ApiResponse<>(ApiResponseReviewEnum.REVIEW_GET_STORE_SUCCESS,
                storeDtoList(reviewRepository.findByStoreIdOrderByCreatedAtDesc(store.getId())));
    }

    // 별점 별 리뷰 조회
    public ApiResponse<List<ReviewRatingResponseDto>> getReviewsByRating(int minRating, int maxRating) {
        // 별점 범위 설정
        minRating = Math.max(minRating, 1);
        maxRating = Math.min(maxRating, 5 );

        return new ApiResponse<>(ApiResponseReviewEnum.REVIEW_GET_RATING_SUCCESS,
                ratingToDtoList(reviewRepository
                        .findByRatingBetweenOrderByCreatedAtDesc(minRating,maxRating)));
    }


    private Order checkOrder(Long orderId) {
        // 주문 번호 체크
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new HandleNotFound(ApiResponseReviewEnum.ORDER_NOT_FOUND));

        // 주문 상태 체크
        if(!order.getStatus().equals(OrderStatusEnum.DELIVERED)){
            throw new InvalidRequestException(ApiResponseReviewEnum.UNAUTHORIZED_ORDER_STATUS);
        }
        return order;
    }

    // 가게 별 리뷰 조회 및 dto 변환
    private List<ReviewResponseDto> storeDtoList(List<Review> reviews) {
        return reviews.stream().map(ReviewResponseDto::from).collect(Collectors.toList());
    }

    // 별점 별 리뷰 조회 및 dto 변환
    private List<ReviewRatingResponseDto> ratingToDtoList(List<Review> reviews) {
        return reviews.stream().map(ReviewRatingResponseDto::from).collect(Collectors.toList());
    }

    // 주문자인지 확인
    private void checkMemberOrder(Order order,Member member) {
//        member = order.getMember();
        if (!member.equals(order.getMember())) {
            throw new HandleUnauthorizedException(ApiResponseOrderEnum.NOT_OWNER);
        }
    }
}
