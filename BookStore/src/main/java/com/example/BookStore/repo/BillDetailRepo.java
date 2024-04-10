package com.example.BookStore.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.BillDetail;


public interface BillDetailRepo extends JpaRepository<BillDetail, Integer> {
	Page<BillDetail> findAll(Pageable pageable);
	
	@Query("SELECT b FROM BillDetail b WHERE b.bill.id = :id")
	List<BillDetail> findByBillId(int id);
}
