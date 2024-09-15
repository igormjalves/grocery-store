package com.grocery_store.payment.repository;

import com.grocery_store.payment.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
