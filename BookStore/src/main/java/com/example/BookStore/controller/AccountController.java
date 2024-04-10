package com.example.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.AccountDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.AccountService;


@RestController
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@PostMapping("/register/")
	public ResponseDTO<Void> create(@RequestBody AccountDTO accountDTO) {		
		accountService.create(accountDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PutMapping("/forgot-password/")
	public ResponseDTO<Void> forgotPassword(
			@RequestParam("username") String username) {
				
		if(accountService.findByUsername(username) == null) {
			return ResponseDTO.<Void>builder()
					.status(200)
					.msg("Username is not exists")
					.build();
		}
		
		accountService.forgotPassword(username);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("Password is sent your email")
					.build();
	}
	
	@PutMapping("/account/reset-password")
	public ResponseDTO<Void> update(
			@RequestParam("password") String password,
			@RequestParam("username") String username) {
		
		accountService.resetPassword(username, password);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PutMapping("/account/")
	public ResponseDTO<Void> update(@RequestBody AccountDTO accountDTO) {
		accountService.update(accountDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	
	@DeleteMapping("/account/{id}")
	public ResponseDTO<Void> delete(@PathVariable int id) {
		accountService.delete(id);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();
	}
	
	@GetMapping("/account/")
	public ResponseDTO<Page<AccountDTO>> getAll(@RequestBody SearchDTO searchDTO) {
		
		return ResponseDTO.<Page<AccountDTO>>builder()
					.status(200)
					.msg("ok")
					.data(accountService.getAll(searchDTO))
					.build();
	}
	
	@PostMapping("/account/search-by-username")
	public ResponseDTO<Page<AccountDTO>> getByUsername(@RequestBody SearchDTO searchDTO){
		
		System.out.println("kw: " + searchDTO.getKeyword());
		
		return ResponseDTO.<Page<AccountDTO>>builder()
				.status(200)
				.msg("ok")
				.data(accountService.search(searchDTO))
				.build(); 
	}
}
