package com.example.nagoyameshi.service;

import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.ShopRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Shop;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    private final ShopRepository shopRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ShopRepository shopRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.shopRepository = shopRepository;
    }

    public boolean toggleFavorite(Integer shopId, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Favorite> favorite = favoriteRepository.findByUserAndShopId(user, shopId);
        Shop shop = shopRepository.getReferenceById(shopId);

        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            return false;
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setShop(shop);
            favoriteRepository.save(newFavorite);
            return true;
        }
    }

    public List<Shop> getUserFavoriteShops(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Favorite> favorites = favoriteRepository.findByUser(user);
        return favorites.stream()
                .map(Favorite::getShop)
                .collect(Collectors.toList());
    }

    public boolean isFavorite(String email, Integer shopId) {
        Optional<User> user = userRepository.findByEmail(email);

        // If user does not exist, return false
        return user.filter(value -> favoriteRepository.existsByUserAndShopId(value, shopId)).isPresent();

    }

}
