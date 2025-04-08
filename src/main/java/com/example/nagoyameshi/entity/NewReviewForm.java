package com.example.nagoyameshi.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class NewReviewForm {
	@Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(max = 500) // コメントの最大長を500文字に制限
    private String comment;

    // Getters and Setters
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
