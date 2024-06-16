package com.web.bakery.repository;

import com.web.bakery.model.FavoriteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<FavoriteDTO, Integer> {
    @Query("SELECT f FROM FavoriteDTO f JOIN BakeryDTO a ON f.aid_id = a.id WHERE f.user_id = :id " +
                "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<FavoriteDTO> getFavs(org.springframework.data.domain.Pageable pageable, @Param("id") UUID id, @Param("search") String search);

    @Query("SELECT COUNT(f) FROM FavoriteDTO f JOIN BakeryDTO a ON f.aid_id = a.id WHERE f.user_id = :id " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))")
    Integer getFavCount(@Param("id") UUID id, @Param("search") String search);

    @Query("SELECT f FROM FavoriteDTO f WHERE user_id=:id AND aid_id = :aid_id")
    Optional<FavoriteDTO> getFavVIaIDs(@Param("id") UUID id, @Param("aid_id") Integer aid);

    @Transactional
    @Modifying
    @Query("DELETE FROM FavoriteDTO WHERE user_id=:id AND aid_id = :aid_id")
    void deleteFavVIaIDs(@Param("id") UUID id, @Param("aid_id") Integer aid);
}
