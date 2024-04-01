package com.example.BookStore.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BookStore.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	Page<User> findAll(Pageable pageable);

	@Query("SELECT u FROM User u WHERE MONTH(u.birthdate) = :month AND DAY(u.birthdate) = :date")
	List<User> searchByBirthDay(int date, int month);
}
