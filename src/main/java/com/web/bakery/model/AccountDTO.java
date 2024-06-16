package com.web.bakery.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "accounts")
@Data
public class AccountDTO {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    public AccountDTO() {
        // Генерируем UUID только если он не задан вручную
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @NotBlank
    @Column(name = "password_hash")
    private String passwordHash;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name="address")
    private String address;

    @PrePersist
    public void prePersist() {
        address = "";
        this.updatedAt = this.createdAt = LocalDateTime.now();
    }
}