package com.grocery_store.payment.repository;

import com.grocery_store.payment.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
