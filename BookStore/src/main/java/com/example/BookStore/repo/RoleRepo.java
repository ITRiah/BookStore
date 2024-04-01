package com.example.BookStore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BookStore.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {
	Page<Role> findAll(Pageable pageable);
}
