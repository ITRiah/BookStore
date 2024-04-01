package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BookStore.entity.BillDetails;


public interface BillDetailsRepo extends JpaRepository<BillDetails, Integer> {
	Page<BillDetails> findAll(Pageable pageable);
}
