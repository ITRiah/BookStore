package com.example.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.AccountDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.AccountService;


@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	AccountService AccountService;
	
	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody AccountDTO accountDTO) {
		System.out.println(accountDTO.getUserName() + accountDTO.getPassWord());
		
		AccountService.create(accountDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody AccountDTO accountDTO) {
		AccountService.update(accountDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable int id) {
		AccountService.delete(id);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<AccountDTO>> getAll(@RequestBody SearchDTO searchDTO) {
		
		return ResponseDTO.<Page<AccountDTO>>builder()
					.status(200)
					.msg("ok")
					.data(AccountService.getAll(searchDTO))
					.build();
	}
	
	@PostMapping("/search-by-username")
	public ResponseDTO<Page<AccountDTO>> getByUsername(@RequestBody SearchDTO searchDTO){
		
		System.out.println("kw: " + searchDTO.getKeyword());
		
		return ResponseDTO.<Page<AccountDTO>>builder()
				.status(200)
				.msg("ok")
				.data(AccountService.search(searchDTO))
				.build(); 
	}
}
