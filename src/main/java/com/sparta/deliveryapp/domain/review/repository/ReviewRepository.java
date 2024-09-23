package com.sparta.deliveryapp.domain.review.repository;

import com.sparta.deliveryapp.entity.Review;
import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStoreIdOrderByCreatedAtDesc(Long storeId);

    List<Review> findByRatingBetweenOrderByCreatedAtDesc(int minRating, int maxRating);
}
