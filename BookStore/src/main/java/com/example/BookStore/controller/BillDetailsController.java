package com.example.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.BillDetailsDTO;
import com.example.BookStore.dto.CartDetailsDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.BillDetailsService;
import com.example.BookStore.service.CartDetailsService;

@RestController
@RequestMapping("/member/bill-details")
public class BillDetailsController {
	
	@Autowired
	BillDetailsService BillDetailsService;
	
	@Autowired
	CartDetailsService cartDetailsService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody BillDetailsDTO BillDetailsDTO) {
		
		BillDetailsService.create(BillDetailsDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<BillDetailsDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<BillDetailsDTO>>builder()
					.status(200)
					.msg("ok")
					.data(BillDetailsService.getAll(searchDTO))
					.build();
	}
}
