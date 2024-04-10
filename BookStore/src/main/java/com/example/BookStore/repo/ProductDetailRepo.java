package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

import com.example.BookStore.entity.ProductDetail;

public interface ProductDetailRepo extends JpaRepository<ProductDetail, Integer> {
	Page<ProductDetail> findAll(Pageable pageable);
	

	@Query("SELECT p FROM ProductDetail p WHERE p.product.id = :id ")
	List<ProductDetail> getByProductId(int id); 
	
	@Query("SELECT p FROM ProductDetail p WHERE p.product.id = :id and p.color = :color")
	ProductDetail getByProductIdColor(int id, String color); 
}
