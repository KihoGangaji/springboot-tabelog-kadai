package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Sales;
import com.example.nagoyameshi.entity.Shop;
import com.example.nagoyameshi.form.ShopEditForm;
import com.example.nagoyameshi.form.ShopRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.SalesRepository;
import com.example.nagoyameshi.repository.ShopRepository;
import com.example.nagoyameshi.service.ShopService;

@Controller
@RequestMapping("/admin/shops")
public class AdminShopController {
    private final ShopRepository shopRepository;
    private final SalesRepository salesRepository;
    private final CategoryRepository categoryRepository;
    private final ShopService shopService;
    
    public AdminShopController(ShopRepository shopRepository, SalesRepository salesRepository, CategoryRepository categoryRepository, ShopService shopService) {
        this.shopRepository = shopRepository;
        this.salesRepository = salesRepository;
        this.categoryRepository = categoryRepository;
        this.shopService = shopService;
    }
    
    // 売上一覧
    @GetMapping("/{id}/sales")
    public String showSales(@PathVariable(name = "id") Integer id, Model model) {
    	//店舗情報を取得
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shop Id:" + id));
        List<Sales> sales = salesRepository.findByShop(shop);  // 売上データを取得

        model.addAttribute("shop", shop);
        model.addAttribute("sales", sales);

        return "admin/shops/sales";  // 売上一覧ページに遷移
    }

    // カテゴリー一覧
    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();  // カテゴリー情報を取得
        model.addAttribute("categories", categories);
        return "admin/shops/categories";  // カテゴリー一覧ページに遷移
    }
    
    @GetMapping
    public String index(Model model, 
                        @PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.ASC) Pageable pageable,
                        @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Shop> shopPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            shopPage = shopRepository.findByShopNameLike("%" + keyword + "%", pageable);                
        } else {
            shopPage = shopRepository.findAll(pageable);
        }

        model.addAttribute("shops", shopPage);
        model.addAttribute("keyword", keyword);
        
        return "admin/shops/index";
    } 
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
    	Shop shop = shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shop Id:" + id));
        
        model.addAttribute("shop", shop);
        
        return "admin/shops/show";
    }    
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("shopRegisterForm", new ShopRegisterForm());
        return "admin/shops/register";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated ShopRegisterForm shopRegisterForm, 
                         BindingResult bindingResult, 
                         RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/shops/register";
        }
        
        shopService.create(shopRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "お店を登録しました。");    
        
        return "redirect:/admin/shops";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
    	Shop shop = shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shop Id:" + id));

        List<Category> categories = categoryRepository.findAll();
        System.out.println("cate" + categories.size());
        String imageName = shop.getImageName();
        ShopEditForm shopEditForm = new ShopEditForm(shop.getId(), shop.getShopName(), null, shop.getCategoryId(),shop.getDescription(), shop.getPrice(), shop.getReserveCount(), shop.getAddress(), shop.getPhoneNumber());
        
        model.addAttribute("imageName", imageName);
        model.addAttribute("shopEditForm", shopEditForm);
        model.addAttribute("categories", categories);

        return "admin/shops/edit";
    } 
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated ShopEditForm shopEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/shops/edit";
        }
        
        shopService.update(shopEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
        
        return "redirect:/admin/shops";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        shopRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/admin/shops";
    }
}
