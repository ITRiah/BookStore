package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BookStore.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	Page<Category> findAll(Pageable pageable);
}
