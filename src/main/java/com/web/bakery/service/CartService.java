package com.web.bakery.service;

import com.web.bakery.model.BakeryDTO;
import com.web.bakery.model.CartItemDTO;
import com.web.bakery.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    CartItemRepository cartRepository;

    public Integer getCartSize(UUID id){return cartRepository.getCartItemsCount(id);}

    public CartItemDTO addToCart(CartItemDTO item) {
        if(cartRepository.getCartItemExisted(item.getUser_id(),item.getAid_id())!=0) {
            cartRepository.addToCartExisted(item.getUser_id(), item.getAid_id(), item.getQuantity());
            return item;
        }
        else return cartRepository.save(item);
    }

    public List<BakeryDTO> getCartAids(UUID id){
        return cartRepository.getAidsFromCart(id);
    }
    public List<CartItemDTO> getCartItems(UUID id){
        return cartRepository.getCartItems(id);
    }
    public CartItemDTO resetCartItemCount(CartItemDTO item)
    {
        cartRepository.resetCartItemCount(item.getUser_id(),item.getAid_id(),item.getQuantity());
        return item;
    }
    public boolean deleteCartItem(CartItemDTO item)
    {
        cartRepository.deleteFromCart(item.getUser_id(),item.getAid_id());
        return true;
    }
}
