package com.aldekain.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderPlacedEvent {
    private String orderId;
}
