package com.web.bakery.repository;

import com.web.bakery.model.AccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountDTO, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE AccountDTO a SET a.isDeleted = true WHERE a.id = :id AND a.isDeleted = false")
    void deleteSoftById(@Param("id") UUID id);

    @Query("SELECT passwordHash FROM AccountDTO WHERE email = :email")
    Optional<String> getPasswordHash(@Param("email") String email);

    @Query("SELECT id FROM AccountDTO WHERE email = :email")
    Optional<UUID> getIdByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE AccountDTO a SET a.updatedAt = :upd WHERE a.id = :id")
    void setUpdatedAtById(@Param("id") UUID id, @Param("upd") LocalDateTime updatedAt);

    @Transactional
    @Modifying
    @Query("UPDATE AccountDTO a SET a.isDeleted = false WHERE a.id = :id AND a.isDeleted = true")
    void recoverUserById(@Param("id") UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE AccountDTO a SET " +
            "a.name = :name, " +
            "a.email = :email, " +
            "a.address = :address " +
            "WHERE a.id = :id")
    void updateUser(@Param("id") UUID id,
                    @Param("name") String name,
                    @Param("email") String email,
                    @Param("address") String address);

    @Transactional
    @Modifying
    @Query("UPDATE AccountDTO a SET " +
            "a.passwordHash = :new_ " +
            "WHERE a.id = :id")
    void resetPassword(@Param("id") UUID id,
                    @Param("new_") String new_);

    @Query("SELECT passwordHash FROM AccountDTO WHERE id = :id")
    String getPassHash(@Param("id") UUID id);

    @Query("SELECT address FROM AccountDTO WHERE id = :id")
    String getAddress(@Param("id") UUID id);

    @Query("SELECT COUNT(*) FROM AccountDTO")
    Integer getUserCount();

    @Query("SELECT u FROM AccountDTO u WHERE isDeleted = FALSE")
    Page<AccountDTO> getActiveUsers(org.springframework.data.domain.Pageable pageable);
}