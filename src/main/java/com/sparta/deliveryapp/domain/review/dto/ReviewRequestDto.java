package com.sparta.deliveryapp.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public class ReviewRequestDto {

    @NotNull(message = "주문번호를 입력해주세요.")
    private final Long orderId;

    @NotNull(message = "별점을 입력해주세요.")
    @Range(min=1, max=5)
    private final int rating;

    @NotBlank(message = "내용을 입력해주세요.")
    private final String contents;

    public ReviewRequestDto(long orderId, int rating, String contents) {
        this.orderId = orderId;
        this.rating = rating;
        this.contents = contents;
    }
}
