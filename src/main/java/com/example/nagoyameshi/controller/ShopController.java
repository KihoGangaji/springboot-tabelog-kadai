package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.ResponseEntity;

import com.example.nagoyameshi.entity.NewReviewForm;
import com.example.nagoyameshi.entity.Review; // 追加
import com.example.nagoyameshi.entity.Shop;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.ShopRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.SubscriptionService;
import com.example.nagoyameshi.service.ReviewService; // 追加
import com.example.nagoyameshi.service.FavoriteService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
 @RequestMapping("/shops")
public class ShopController {
     private final ShopRepository shopRepository;

     private final FavoriteService favoriteService;
     private final SubscriptionService subscriptionService;
     private final ReviewService reviewService; // 追加
     private final UserRepository userRepository; // 追加
     
     public ShopController(ShopRepository shopRepository, ReviewService reviewService, UserRepository userRepository, FavoriteService favoriteService, SubscriptionService subscriptionService) {
         this.shopRepository = shopRepository;
         this.reviewService = reviewService; // 追加
         this.userRepository = userRepository; // 追加
         this.favoriteService = favoriteService;
         this.subscriptionService = subscriptionService;
     }     
   
     @GetMapping
     public String index(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "area", required = false) String area,
                         @RequestParam(name = "price", required = false) Integer price,
                         @RequestParam(name = "order", required = false) String order,
                         @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                         Model model) 
     {
         Page<Shop> shopPage;
                 
         if (keyword != null && !keyword.isEmpty()) {
        	 if (order != null && order.equals("priceAsc")) {
                 shopPage = shopRepository.findByShopNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
             } else {
            	 shopPage = shopRepository.findByShopNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
             }
         } else if (area != null && !area.isEmpty()) {
        	 if (order != null && order.equals("priceAsc")) {
                 shopPage = shopRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
             } else {
            	 shopPage = shopRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
             }
         } else if (price != null) {
        	 if (order != null && order.equals("priceAsc")) {
                 shopPage = shopRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
             } else {
                 shopPage = shopRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
             }
         } else {
        	 if (order != null && order.equals("priceAsc")) {
                 shopPage = shopRepository.findAllByOrderByPriceAsc(pageable);
             } else {
                 shopPage = shopRepository.findAllByOrderByCreatedAtDesc(pageable);   
             }            
         }
        	 
         model.addAttribute("shopPage", shopPage);
         model.addAttribute("keyword", keyword);
         model.addAttribute("area", area);
         model.addAttribute("price", price);
         model.addAttribute("order", order);
         
         return "shops/index";
     }


     @PostMapping("/{shopId}/favorite")
     public String  makeFavorite(@PathVariable Integer shopId, @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes){
         System.out.println("Favorite action triggered for Shop ID: " + shopId);
         if (userDetails == null) {
             model.addAttribute("errorMessage", "ログインしてください。");
             return "redirect:/login"; // ログインページにリダイレクト
         }

         boolean isFavorite = favoriteService.toggleFavorite(shopId, userDetails.getUsername());

         if (isFavorite) {
             redirectAttributes.addFlashAttribute("successMessage", "店舗をお気に入りに追加しました！"); // "Shop added to favorites!"
         } else {
             redirectAttributes.addFlashAttribute("successMessage", "店舗をお気に入りから削除しました！"); // "Shop removed from favorites!"
         }

         return "redirect:/shops/" + shopId;
     }

    @GetMapping("/favorites")
    public String getFavoriteShops(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (userDetails == null) {
            model.addAttribute("errorMessage", "ログインしてください。");
            return "redirect:/login";
        }

        List<Shop> favoriteShops = favoriteService.getUserFavoriteShops(userDetails.getUsername());

        model.addAttribute("favoriteShops", favoriteShops);
        return "shops/favorites"; // Updated template
    }

     @GetMapping("/{id}")
     public String show(@PathVariable(name = "id") Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
         Shop shop = shopRepository.findById(id).orElse(null);
         if (shop == null) {
             model.addAttribute("errorMessage", "店舗情報が見つかりません。");
             return "error"; // エラーページにリダイレクト
         }
         
         List<Review> reviews = reviewService.getReviewsByShopId((int) id); // レビューを取得
         model.addAttribute("reviews", reviews);
         
         Integer averageRating = reviewService.getAverageRatingByShopId((int) id); // 平均評価を取得

         boolean isFavorite = false;
         boolean hasSubscription = false;

         if (userDetails != null) { // Check if user is logged in
             isFavorite = favoriteService.isFavorite(userDetails.getUsername(), id);

             User user = userRepository.findByEmail(userDetails.getUsername())
                     .orElse(null);

             if (user != null) {
                 // Check if user has active subscription
                 hasSubscription = subscriptionService.hasActiveSubscription(user);
             }
         }

         model.addAttribute("shop", shop); 
         model.addAttribute("reservationInputForm", new ReservationInputForm());
         model.addAttribute("averageRating", averageRating); // 平均評価をモデルに追加
         System.out.println("ar" + averageRating);
         // NewReviewFormオブジェクトをモデルに追加
         model.addAttribute("review", new NewReviewForm());
         model.addAttribute("isFavorite", isFavorite);
         model.addAttribute("hasSubscription", hasSubscription); // Add subscription status


         return "shops/show";
     }


    @GetMapping("/{id}/review")
     public String createReview(@PathVariable(name = "id") Integer id,Model model) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null) {
            model.addAttribute("errorMessage", "店舗情報が見つかりません。");
            return "error"; // エラーページにリダイレクト
        }

        model.addAttribute("shop", shop);
        model.addAttribute("newReviewForm", new NewReviewForm());
        return "shops/review";
     }

     @PostMapping("/{id}/reviews")
     public String addReview(@PathVariable(name = "id") Integer shopId,
    		                 @ModelAttribute NewReviewForm newReviewForm,
    		                 @AuthenticationPrincipal UserDetails userDetails, // ユーザー情報を取得
    		                 Model model,RedirectAttributes redirectAttributes) {
    	 if (userDetails == null) {
    	        model.addAttribute("errorMessage", "ログインしてください。");
    	        return "redirect:/login"; // ログインページにリダイレクト
    	    }
    	 
    	 try {
    		// 新しいレビューの値をログに出力
    	    System.out.println("Rating: " + newReviewForm.getRating());
    	    System.out.println("Comment: " + newReviewForm.getComment());
    		
    	    // Reviewの設定
            Review review = createReview(shopId, newReviewForm, userDetails.getUsername());
            reviewService.saveReview(review); // レビューを保存
             redirectAttributes.addFlashAttribute("successMessage", "レビューが追加されました。");

             return "redirect:/shops/" + shopId; // 店舗詳細ページにリダイレクト
    	    } catch (IllegalArgumentException e) {
             System.out.println("errorMessage first" + e.getMessage());
    	        model.addAttribute("errorMessage", e.getMessage());
    	    } catch (Exception e) {
             System.out.println("errorMessage second");
    	        model.addAttribute("errorMessage", "レビューの投稿に失敗しました: " + e.getMessage());
    	    }
    	    // エラーメッセージがある場合はショップ情報を再設定して表示
    	    return prepareShopModel(shopId, model);
    	}
        private Review createReview(Integer shopId, NewReviewForm newReviewForm, String username) {
    	    Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new IllegalArgumentException("店舗が見つかりません"));
    	    User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

    	    Review review = new Review();
    	    review.setShop(shop);
    	    review.setUser(user);
    	    review.setRating(newReviewForm.getRating());
    	    review.setComment(newReviewForm.getComment());
    	    return review;
    	}

    @GetMapping("/{shopId}/reviews/{reviewId}/edit")
    public String editReviewForm(@PathVariable(name = "shopId") Integer shopId,
                                 @PathVariable(name = "reviewId") Integer reviewId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        if (userDetails == null) {
            model.addAttribute("errorMessage", "ログインしてください。");
            return "redirect:/login";
        }

        Shop shop = shopRepository.findById(shopId).orElse(null);
        if (shop == null) {
            model.addAttribute("errorMessage", "店舗情報が見つかりません。");
            return "error";
        }

        Review review = reviewService.findById(reviewId);
        if (!review.getUser().getEmail().equals(userDetails.getUsername())) {
            model.addAttribute("errorMessage", "レビューが見つからないか、編集権限がありません。");
            return "error";
        }

        NewReviewForm form = new NewReviewForm();
        form.setRating(review.getRating());
        form.setComment(review.getComment());

        model.addAttribute("shop", shop);
        model.addAttribute("review", review);
        model.addAttribute("newReviewForm", form);
        return "shops/review-edit";
    }

    @PostMapping("/{shopId}/reviews/{reviewId}/update")
    public String updateReview(@PathVariable(name = "shopId") Integer shopId,
                               @PathVariable(name = "reviewId") Integer reviewId,
                               @ModelAttribute NewReviewForm newReviewForm,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model, RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            model.addAttribute("errorMessage", "ログインしてください。");
            return "redirect:/login";
        }

        try {
            Review existingReview = reviewService.findById(reviewId);
            if (!existingReview.getUser().getEmail().equals(userDetails.getUsername())) {
                throw new IllegalArgumentException("レビューが見つからないか、編集権限がありません。");
            }

            // レビューを更新
            existingReview.setRating(newReviewForm.getRating());
            existingReview.setComment(newReviewForm.getComment());
            reviewService.saveReview(existingReview);
            redirectAttributes.addFlashAttribute("successMessage", "レビューが更新されました");

            return "redirect:/shops/" + shopId;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "レビューの更新に失敗しました: " + e.getMessage());
        }

        return prepareShopModel(shopId, model);
    }

    @PostMapping("/{shopId}/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Integer shopId,
                               @PathVariable Integer reviewId,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            Review review = reviewService.findById(reviewId);

            // Check if current user is the review author
            if (!review.getUser().getEmail().equals(userDetails.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "自分のレビューのみ削除できます");
                return "redirect:/shops/" + shopId;
            }

            reviewService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "レビューの削除に失敗しました: " + e.getMessage());
        }

        return "redirect:/shops/" + shopId;
    }

    	private String prepareShopModel(Integer shopId, Model model) {
    	    Shop shop = shopRepository.findById(shopId).orElse(null);
    	    if (shop != null) {
    	        List<Review> reviews = reviewService.getReviewsByShopId(shopId);
    	        Integer averageRating = reviewService.getAverageRatingByShopId(shopId);
    	        
    	        model.addAttribute("shop", shop);
    	        model.addAttribute("reviews", reviews);
    	        model.addAttribute("averageRating", averageRating);
    	        model.addAttribute("reservationInputForm", new ReservationInputForm());
    	    } else {
    	        model.addAttribute("errorMessage", "店舗情報が見つかりません。");
    	        return "error"; // エラーページにリダイレクト
    	    }
    	    return "shops/show";
    	}

 }