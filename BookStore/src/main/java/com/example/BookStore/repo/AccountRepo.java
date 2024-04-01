package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BookStore.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Integer> {
	Page<Account> findAll(Pageable pageable);
	
	Account findByUserName(String username);
}
