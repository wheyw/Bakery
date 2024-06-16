package com.web.bakery.repository;

import com.web.bakery.model.SaleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<SaleDTO, Integer> {
}
