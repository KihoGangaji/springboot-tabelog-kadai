package com.example.nagoyameshi.form;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
    @NotBlank(message = "予約日時を選択してください。")
    private String reserveDateTime;

    @NotNull(message = "人数を入力してください。")
    @Min(value = 1, message = "人数は1人以上に設定してください。")
    private Integer numberOfPeople;
    
    // shopId フィールドを追加
    private Long shopId; // 適切な型を使用してください
    
    // userId フィールドを追加
    private Long userId; // 適切な型を使用してください


    
    // LocalDateTime形式で予約日時を取得するメソッド
    public LocalDateTime getReserveDateTimeAsLocalDateTime() {
        try {
            if (this.reserveDateTime == null || this.reserveDateTime.isEmpty()) {
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(this.reserveDateTime, formatter);
        } catch (Exception e) {
            return null;
        }

    }
}
    
