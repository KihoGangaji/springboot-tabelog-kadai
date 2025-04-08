package com.example.nagoyameshi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
    private Shop shop; // どの店舗に対するレビューか
	
	@ManyToOne
	@NotNull
    private User user; // ユーザー名またはレビューを書いた人
	
	@NotNull
    @Min(1)
    @Max(5)
    private Integer rating; // 星の評価（1から5）

    @Column(length = 500) // コメントの最大長
    private String comment;

    private LocalDateTime createdAt; // 作成日時
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // レビュー作成時に自動で日時を設定
    }
}
