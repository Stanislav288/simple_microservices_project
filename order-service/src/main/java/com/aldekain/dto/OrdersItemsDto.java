package com.aldekain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdersItemsDto {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
