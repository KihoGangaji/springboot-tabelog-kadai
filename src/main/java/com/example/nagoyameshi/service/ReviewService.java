package com.example.nagoyameshi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByShopId(Integer shopId) {
        return reviewRepository.findByShopId(shopId);
    }

    public void saveReview(Review review) {
        review.setCreatedAt(LocalDateTime.now()); // 作成日時を設定
        reviewRepository.save(review);
    }

    // 追加: 平均評価を取得するメソッド
    public Integer getAverageRatingByShopId(Integer shopId) {
        return reviewRepository.findAverageRatingByShopId(shopId);
    }

    @NonNull
    public Review findById(@NonNull Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
    }

    @Transactional
    public void deleteReview(@NonNull Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
