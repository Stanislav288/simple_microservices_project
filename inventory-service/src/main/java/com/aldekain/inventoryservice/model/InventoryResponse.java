package com.aldekain.inventoryservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;
}
