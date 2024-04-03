package com.example.BookStore.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer> {
	Page<Bill> findAll(Pageable pageable);
	
	Page<Bill> findByStatus(String status,Pageable pageable);
	
	@Query("SELECT b FROM Bill b WHERE b.user.id = :id")
	List<Bill> getByUserId(int id);
}
