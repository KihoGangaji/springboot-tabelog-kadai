package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Shop;
import com.example.nagoyameshi.form.ShopEditForm;
import com.example.nagoyameshi.form.ShopRegisterForm;
import com.example.nagoyameshi.repository.ShopRepository;

@Service
public class ShopService {
    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Transactional
    public void create(ShopRegisterForm shopRegisterForm) {
        // Shopエンティティのインスタンスを作成
        Shop shop = new Shop();

        // 画像ファイルがアップロードされている場合の処理
        MultipartFile imageFile = shopRegisterForm.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            System.out.println("Attempting to save to: " + filePath.toAbsolutePath());


            try {
                Files.createDirectories(filePath.getParent()); // ディレクトリが存在しない場合は作成
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                shop.setImageName(hashedImageName); // 保存した画像ファイル名をセット
            } catch (IOException e) {
                System.out.println("image error" + e);
                throw new RuntimeException("Failed to store image file", e);
            }
        } else {
            shop.setImageName(""); // 画像がない場合は空の文字列
        }

        // フォームデータをエンティティに設定
        shop.setShopName(shopRegisterForm.getShopName());
        shop.setDescription(shopRegisterForm.getDescription());
        shop.setPrice(shopRegisterForm.getPrice());
        shop.setReserveCount(shopRegisterForm.getReserveCount()); // reserveCountをセット
        shop.setAddress(shopRegisterForm.getAddress());
        shop.setPhoneNumber(shopRegisterForm.getPhoneNumber());
        shop.setCategoryId(shopRegisterForm.getCategoryId());
        System.out.println(shop + "check shop");
        // Shopエンティティを保存
        shopRepository.save(shop);
    }

    @Transactional
    public void update(ShopEditForm shopEditForm) {
        // 既存のShopエンティティを取得
        Shop shop = shopRepository.getReferenceById(shopEditForm.getId());

        // 画像ファイルがアップロードされている場合の処理
        MultipartFile imageFile = shopEditForm.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);

            try {
                Files.createDirectories(filePath.getParent()); // ディレクトリが存在しない場合は作成
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                shop.setImageName(hashedImageName); // 新しい画像ファイル名をセット
            } catch (IOException e) {
                // エラー処理を追加
                e.printStackTrace();
            }
        }

        // フォームデータを更新
        shop.setShopName(shopEditForm.getShopname());
        shop.setDescription(shopEditForm.getDescription());
        shop.setPrice(shopEditForm.getPrice());
        shop.setReserveCount(shopEditForm.getReserveCount()); // reserveCountを更新
        shop.setAddress(shopEditForm.getAddress());
        shop.setPhoneNumber(shopEditForm.getPhoneNumber());
        shop.setCategoryId(shopEditForm.getCategoryId());

        // Shopエンティティを保存
        shopRepository.save(shop);
    }

    private String generateNewFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        return uniqueFileName;
    }
}
