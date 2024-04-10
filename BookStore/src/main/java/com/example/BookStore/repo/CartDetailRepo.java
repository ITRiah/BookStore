package com.example.BookStore.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.CartDetail;

public interface CartDetailRepo extends JpaRepository<CartDetail, Integer> {
	Page<CartDetail> findAll(Pageable pageable);

	@Query("SELECT p FROM CartDetail p WHERE p.product.id = :id ")
	List<CartDetail> getByProductId(int id); 
	
	@Query("SELECT p FROM CartDetail p WHERE p.product.id = :id AND p.color = :color")
	CartDetail getByProductIdAndColor(int id, String color); 
}
