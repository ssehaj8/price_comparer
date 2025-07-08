package com.project.price_comparer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceResult {
    private String link;
    private String price;
    private String currency;
    private String productName;
    private String seller;
}
