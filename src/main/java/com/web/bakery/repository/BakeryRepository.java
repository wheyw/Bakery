package com.web.bakery.repository;

import com.web.bakery.model.BakeryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BakeryRepository extends JpaRepository<BakeryDTO, Integer> {
    @Query("SELECT u FROM BakeryDTO u WHERE isDeleted = FALSE")
    Page<BakeryDTO> getActiveAids(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT u FROM BakeryDTO u WHERE isDeleted = FALSE AND discountPercent > 0 ORDER BY discountPercent DESC")
    List<BakeryDTO> getSales();

    @Query("SELECT a FROM BakeryDTO a WHERE a.isDeleted = false " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY (a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) ASC")
    Page<BakeryDTO> searchAids(org.springframework.data.domain.Pageable pageable, @Param("search") String search);

    @Query("SELECT a FROM BakeryDTO a WHERE a.isDeleted = false " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))" +
            "AND ((a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) BETWEEN :minPrice AND :maxPrice) ORDER BY (a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) ASC")
    Page<BakeryDTO> searchAidsWithPriceRangeOrderByPriceAsc(org.springframework.data.domain.Pageable pageable,
                                                            @Param("search") String search,
                                                            @Param("minPrice") double minPrice,
                                                            @Param("maxPrice") double maxPrice);


    @Transactional
    @Modifying
    @Query("UPDATE BakeryDTO a SET a.isDeleted = true WHERE a.id = :id AND a.isDeleted = false")
    void deleteSoftById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE BakeryDTO a SET a.isDeleted = false WHERE a.id = :id AND a.isDeleted = true")
    void recoverAidById(@Param("id") Integer id);

    //@Transactional
    //@Modifying
    //@Query("UPDATE AidDTO SET " +
    //        "name = :name, " +
    //        "manufacturer = :manufacturer " +
    //        "imageURL = :imageURL " +
    //        "description = :description " +
    //        "price = :price " +
    //        "quantity = :quantity " +
    //        "WHERE id = :id")
    //void updateAid(@Param("id") Integer id,
    //               @Param("name") String name,
    //               @Param("manufacturer") String manufacturer,
    //               @Param("imageURL") String imageURL,
    //               @Param("description") String description,
    //               @Param("price") double price,
    //               @Param("quantity") Integer quantity);
    @Query("SELECT COUNT(*) FROM BakeryDTO")
    Integer getAidsCount();

    @Query("SELECT COUNT(a) FROM BakeryDTO a WHERE a.isDeleted = false AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))")
    Integer getSearchAidsCount(@Param("search") String search);

    @Query("SELECT COUNT(a) FROM BakeryDTO a WHERE a.isDeleted = false " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND ((a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) BETWEEN :minPrice AND :maxPrice)")
    Integer countSearchAidsWithPriceRangeOrderByPriceAsc(@Param("search") String search,
                                                         @Param("minPrice") double minPrice,
                                                         @Param("maxPrice") double maxPrice);
}
