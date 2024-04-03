package com.example.BookStore.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.CartDetails;

public interface CartDetailsRepo extends JpaRepository<CartDetails, Integer> {
	Page<CartDetails> findAll(Pageable pageable);

	@Query("SELECT p FROM CartDetails p WHERE p.product.id = :id ")
	List<CartDetails> getByProductId(int id); 
	
	@Query("SELECT p FROM CartDetails p WHERE p.product.id = :id AND p.color = :color")
	CartDetails getByProductIdAndColor(int id, String color); 
}
