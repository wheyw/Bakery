package com.web.bakery.controller;

import com.web.bakery.model.CartItemDTO;
import com.web.bakery.service.CartService;
import com.web.bakery.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartSize(@RequestParam String jwt)
    {
        if(JwtService.validateToken(jwt))
            return ResponseEntity.ok().body(cartService.getCartSize(JwtService.getUserIdFromToken(jwt)));
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @PostMapping("/add")
    public ResponseEntity<CartItemDTO> addToCart(@Valid @RequestBody CartItemDTO request,
                                                 BindingResult bindingResult,
                                                 @RequestParam String jwt) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        if(JwtService.validateToken(jwt)) {
            request.setUser_id(JwtService.getUserIdFromToken(jwt));
            CartItemDTO item = cartService.addToCart(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems(@RequestParam String jwt)
    {
        if(JwtService.validateToken(jwt))
        {
            return ResponseEntity.ok(cartService.getCartItems(JwtService.getUserIdFromToken(jwt)));
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @PutMapping("/reset")
    public ResponseEntity<CartItemDTO> resetCartItemCount(@Valid @RequestBody CartItemDTO request,
                                                          BindingResult bindingResult,
                                                          @RequestParam String jwt)
    {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        if(JwtService.validateToken(jwt)) {
            request.setUser_id(JwtService.getUserIdFromToken(jwt));
            CartItemDTO item = cartService.resetCartItemCount(request);
            return ResponseEntity.status(HttpStatus.OK).body(item);
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteCartItem(@Valid @RequestBody CartItemDTO request,
                                                          BindingResult bindingResult,
                                                          @RequestParam String jwt)
    {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        if(JwtService.validateToken(jwt)) {
            request.setUser_id(JwtService.getUserIdFromToken(jwt));
            Boolean item = cartService.deleteCartItem(request);
            return ResponseEntity.status(HttpStatus.OK).body(item);
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
}
