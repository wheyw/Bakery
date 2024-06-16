package com.web.bakery.repository;

import com.web.bakery.model.HistoryItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryItemDTO, Integer> {

    @Query("SELECT c FROM HistoryItemDTO c WHERE c.user_id=:id AND c.status=0 ORDER BY c.id DESC")
    List<HistoryItemDTO> getActiveDeliveries(@Param("id") UUID id);

    @Query("SELECT c FROM HistoryItemDTO c JOIN BakeryDTO a ON c.aid_id = a.id WHERE (c.user_id=:id AND c.status=1) AND " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY c.id DESC")
    Page<HistoryItemDTO> getHistory(org.springframework.data.domain.Pageable pageable, @Param("id") UUID id, @Param("search") String search);

    @Query("SELECT COUNT(c) FROM HistoryItemDTO c JOIN BakeryDTO a ON c.aid_id = a.id WHERE (c.user_id=:id AND c.status=1) AND " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))")
    Integer getHistorySize(@Param("id") UUID id, @Param("search") String search);

    @Transactional
    @Modifying
    @Query("UPDATE HistoryItemDTO SET status=1 WHERE id=:id AND user_id = :user")
    void confirmItem(@Param("user") UUID user, @Param("id") Integer id);
}
