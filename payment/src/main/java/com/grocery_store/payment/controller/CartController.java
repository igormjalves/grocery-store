package com.grocery_store.payment.controller;

import com.grocery_store.payment.dto.ProductRequest;
import com.grocery_store.payment.model.Cart;
import com.grocery_store.payment.model.CartStatus;
import com.grocery_store.payment.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Validated
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody @Valid List<@Valid ProductRequest> products) {
        Cart cart = cartService.create(products);
        return ResponseEntity.ok(cart);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("add/{id}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long id, @RequestBody @Valid List<ProductRequest> products) {
        Cart cart = cartService.addProductsToCart(id, products, true);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("remove/{id}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long id, @RequestBody @Valid List<ProductRequest> products) {
        Cart cart = cartService.addProductsToCart(id, products, false);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("checkout/{id}")
    public ResponseEntity<Cart> changeCartStatusToCheckout(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.changeCartStatus(id, CartStatus.CHECKOUT));
    }

    @PatchMapping("completed/{id}")
    public ResponseEntity<Cart> changeCartStatusToCompleted(@PathVariable Long id) {
        Cart cart = cartService.changeCartStatus(id, CartStatus.COMPLETED);
        return ResponseEntity.ok(cart);
    }
}
