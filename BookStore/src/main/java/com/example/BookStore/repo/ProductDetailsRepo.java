package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

import com.example.BookStore.entity.ProductDetails;

public interface ProductDetailsRepo extends JpaRepository<ProductDetails, Integer> {
	Page<ProductDetails> findAll(Pageable pageable);
	

	@Query("SELECT p FROM ProductDetails p WHERE p.product.id = :id ")
	List<ProductDetails> getByProductId(int id); 
	
	@Query("SELECT p FROM ProductDetails p WHERE p.product.id = :id and p.color = :color")
	ProductDetails getByProductIdColor(int id, String color); 
}
