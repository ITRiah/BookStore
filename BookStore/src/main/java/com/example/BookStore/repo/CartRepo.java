package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BookStore.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Integer> {
	Page<Cart> findAll(Pageable pageable);
}
