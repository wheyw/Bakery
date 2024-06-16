package com.web.bakery.repository;

import com.web.bakery.model.BakeryDTO;
import com.web.bakery.model.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemDTO, Integer> {
    @Query("SELECT COUNT(*) FROM CartItemDTO WHERE user_id=:id")
    Integer getCartItemsCount(@Param("id") UUID id);

    @Query("SELECT c FROM CartItemDTO c WHERE c.user_id=:id")
    List<CartItemDTO> getCartItems(@Param("id") UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE CartItemDTO SET quantity = (quantity + :count) WHERE aid_id=:aid_id AND user_id=:user_id")
    void addToCartExisted(@Param("user_id") UUID user_id, @Param("aid_id") Integer aid_id, @Param("count") Integer count);

    @Transactional
    @Modifying
    @Query("UPDATE CartItemDTO SET quantity = :count WHERE aid_id=:aid_id AND user_id=:user_id")
    void resetCartItemCount(@Param("user_id") UUID user_id, @Param("aid_id") Integer aid_id, @Param("count") Integer count);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItemDTO WHERE aid_id=:aid_id AND user_id=:user_id")
    void deleteFromCart(@Param("user_id") UUID user_id, @Param("aid_id") Integer aid_id);

    @Query("SELECT COUNT(*) FROM CartItemDTO WHERE user_id= :user_id AND aid_id=:aid_id")
    Integer getCartItemExisted(@Param("user_id") UUID id, @Param("aid_id") Integer aid_id);

    @Query("SELECT a FROM CartItemDTO ci JOIN BakeryDTO a ON ci.aid_id = a.id AND ci.user_id = :id")
    List<BakeryDTO> getAidsFromCart(@Param("id") UUID id);
}
