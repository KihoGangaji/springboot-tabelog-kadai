package com.example.nagoyameshi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.nagoyameshi.entity.Sales;
import com.example.nagoyameshi.entity.Shop;

public interface SalesRepository extends JpaRepository<Sales, Long> {

    // 合計金額を取得するクエリ
    @Query("SELECT SUM(s.amount) FROM Sales s")
    Double sumByAmount();

    // 日付範囲で売上を取得するクエリ
    List<Sales> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<Sales> findByShop(Shop shop);
}
