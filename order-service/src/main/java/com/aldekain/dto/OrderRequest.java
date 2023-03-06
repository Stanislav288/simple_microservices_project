package com.aldekain.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrdersItemsDto> orderLineItemsDtoList;
}
