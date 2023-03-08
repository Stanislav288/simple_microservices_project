package com.aldekain.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrdersItems> orderLineItemsDtoList;
}
