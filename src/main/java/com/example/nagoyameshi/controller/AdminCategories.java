package com.example.nagoyameshi.controller;

import com.example.nagoyameshi.entity.Category;
import org.springframework.validation.BindingResult;
import com.example.nagoyameshi.repository.CategoryRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategories {
    private final CategoryRepository categoryRepository;

    public AdminCategories(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;

    }
    @GetMapping("")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();  // カテゴリー情報を取得
        model.addAttribute("categories", categories);
        return "admin/shops/categories";  // カテゴリー一覧ページに遷移
    }

    @GetMapping("/{id}/edit")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model) {
        Category category = categoryRepository.getReferenceById(id);
        model.addAttribute("category", category);
        return "admin/shops/edit-categories";
    }

    @PostMapping("/{id}/update")
    public String updateCategory(@PathVariable(name = "id") Integer id,
                                 @ModelAttribute("category") Category category,
                                 RedirectAttributes redirectAttributes) {
        Category existingCategory = categoryRepository.getReferenceById(id);
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);

        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーが更新されました。");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーが削除されました。");
        return "redirect:/admin/categories";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/shops/add-category";
    }

    @PostMapping("/store")
    public String storeCategory(
            @ModelAttribute("category") Category category,  // No @Valid, no BindingResult
            RedirectAttributes redirectAttributes
    ) {
        // Simply save the category (no validation checks)
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーが追加されました。");
        return "redirect:/admin/categories";
    }
}
