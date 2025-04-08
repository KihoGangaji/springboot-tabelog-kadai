package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "shops")
@Data
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 店舗名 */
    @Column(name = "shop_name")
    private String shopName;

    /** 画像ファイル名 */
    @Column(name = "image_name")
    private String imageName;

    /** 店舗の説明 */
    @Column(name = "description")
    private String description;

    /** 平均価格 */
    @Column(name = "price")
    private Integer price;

    /** 現在の予約数 */
    @Column(name = "reserve_cnt")
    private Integer reserveCount;

    /** 店舗の住所 */
    @Column(name = "address")
    private String address;

    /** 電話番号 */
    @Column(name = "phone_number")
    private String phoneNumber;

    /** 作成日時 */
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    /** 更新日時 */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

    /** カテゴリーID */
    @Column(name = "category_id")
    private Integer categoryId;
}
