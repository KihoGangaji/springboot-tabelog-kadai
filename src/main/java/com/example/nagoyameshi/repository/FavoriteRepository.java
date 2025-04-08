package com.example.nagoyameshi.repository;

import org.springframework.stereotype.Repository;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByUserAndShopId(User user, Integer shopId);

    List<Favorite> findByUser(User user);

    boolean existsByUserAndShopId(User user, Integer shopId);
}


