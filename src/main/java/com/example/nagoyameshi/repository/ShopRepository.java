package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
	public Page<Shop> findByShopNameLike(String keyword, Pageable pageable);
	
	public Page<Shop> findByShopNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Shop> findByShopNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Shop> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
    public Page<Shop> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
    public Page<Shop> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
    public Page<Shop> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable); 
    public Page<Shop> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<Shop> findAllByOrderByPriceAsc(Pageable pageable);
    
    public List<Shop> findTop10ByOrderByCreatedAtDesc();
}
