package com.aldekain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inventory_id")
    private UUID id;
    private String skuCode;
    private Integer quantity;
}
