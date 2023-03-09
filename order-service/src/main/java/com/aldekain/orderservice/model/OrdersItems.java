package com.aldekain.orderservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdersItems {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
