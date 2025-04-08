package com.example.nagoyameshi.repository;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.example.nagoyameshi.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByShopId(Integer shopId); // 店舗IDでレビューを取得

    // 店舗IDでの平均評価を取得するメソッド
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.shop.id = ?1")
    Integer findAverageRatingByShopId(Integer shopId);

    @Override
    @NonNull
    Optional<Review> findById(@NonNull Integer id);

    @Override
    void deleteById(@NonNull Integer id);
}
