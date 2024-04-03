package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	Page<Product> findAll(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE :name")
	Page<Product> searchByName(String name, Pageable pageable);
}
