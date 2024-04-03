package com.example.BookStore.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.BillDetails;


public interface BillDetailsRepo extends JpaRepository<BillDetails, Integer> {
	Page<BillDetails> findAll(Pageable pageable);
	
	@Query("SELECT b FROM BillDetails b WHERE b.bill.id = :id")
	List<BillDetails> findByBillId(int id);
}
