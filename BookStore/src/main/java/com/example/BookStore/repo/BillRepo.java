package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BookStore.entity.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer> {
	Page<Bill> findAll(Pageable pageable);
}
