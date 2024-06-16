package com.web.bakery.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "sales")
@Entity
public class SaleDTO {
    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bakery_id")
    private Integer aid_id;

    @Column(name = "SALE_PERCENT")
    private Integer salePercent;

    @Column(name = "valid_until", updatable = false)
    private LocalDateTime validUntil;
}
