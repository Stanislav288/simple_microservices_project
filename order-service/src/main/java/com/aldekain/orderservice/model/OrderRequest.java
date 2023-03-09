package com.aldekain.orderservice.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrdersItems> orderLineItemsDtoList;
}
