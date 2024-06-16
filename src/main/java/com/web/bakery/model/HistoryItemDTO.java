package com.web.bakery.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history_item")
@Data
public class HistoryItemDTO {
    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "bakery_id")
    private Integer aid_id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private Integer status;

    @Column(name = "price")
    private double price;

    @CreationTimestamp
    @Column(name = "bought_at", updatable = false)
    private LocalDateTime boughtAt;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @PrePersist
    public void prePersist() {
        this.boughtAt = LocalDateTime.now();
        this.status = 0;
        this.deliveryDate = LocalDateTime.now().plusDays(2);
    }
}
