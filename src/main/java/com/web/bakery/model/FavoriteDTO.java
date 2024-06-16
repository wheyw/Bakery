package com.web.bakery.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "favorite_item")
@Data
public class FavoriteDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "bakery_id")
    private Integer aid_id;
}
