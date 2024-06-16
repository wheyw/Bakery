package com.web.bakery.repository;

import com.web.bakery.model.HistoryItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryItemRepository extends JpaRepository<HistoryItemDTO, Integer> {
}
