package com.example.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.CartDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.CartService;

@RestController
@RequestMapping("/member/cart")
public class CartController {
	
	@Autowired
	CartService CartService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody CartDTO CartDTO) {
		CartService.create(CartDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<CartDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<CartDTO>>builder()
					.status(200)
					.msg("ok")
					.data(CartService.getAll(searchDTO))
					.build();
	}
}
