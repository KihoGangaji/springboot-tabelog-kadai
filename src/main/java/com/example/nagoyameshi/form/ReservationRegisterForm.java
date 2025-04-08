package com.example.nagoyameshi.form;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ReservationRegisterForm {    
    private Integer shopId;
    
    private Integer userId;    
        
    private String  reserveDateTime;    
    
    private Integer numberOfPeople;
        
}
    

