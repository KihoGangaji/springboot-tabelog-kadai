package com.example.nagoyameshi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(name = "name")
	    private String name;  // カテゴリー名

	    // GetterとSetterを追加
	}
