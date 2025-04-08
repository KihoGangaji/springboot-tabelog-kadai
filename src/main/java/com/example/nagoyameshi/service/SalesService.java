package com.example.nagoyameshi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Sales;
import com.example.nagoyameshi.repository.SalesRepository;

@Service
public class SalesService {
    @Autowired
    private SalesRepository salesRepository;

    public Double getTotalSalesAmount1() {
        return salesRepository.sumByAmount(); // 合計金額を Double 型で取得
    }

    public Integer getTotalSalesAmount() {
        Double totalAmount = salesRepository.sumByAmount(); // 合計金額を Double 型で取得
        return (totalAmount != null) ? totalAmount.intValue() : 0; // null チェックと Integer 型に変換
    }

    // 同名のメソッドを一つにまとめました
    public Integer getTotalSalesAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Sales> salesList = salesRepository.findBySaleDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        return salesList.stream()
                        .mapToInt(sale -> sale.getAmount() != null ? sale.getAmount().intValue() : 0)
                        .sum();
    }


    // 追加したメソッド: 全ての売上を取得
    public List<Sales> getAllSales() {
        return salesRepository.findAll(); // 全ての売上データを取得
    }
}
