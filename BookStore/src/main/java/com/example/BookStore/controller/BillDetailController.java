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

import com.example.BookStore.dto.BillDetailDTO;
import com.example.BookStore.dto.CartDetailDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.BillDetailService;
import com.example.BookStore.service.CartDetailService;

@RestController
@RequestMapping("/member/bill-details")
public class BillDetailController {
	
	@Autowired
	BillDetailService billDetailsService;
	
	@Autowired
	CartDetailService cartDetailsService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody BillDetailDTO BillDetailsDTO) {
		
		billDetailsService.create(BillDetailsDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<BillDetailDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<BillDetailDTO>>builder()
					.status(200)
					.msg("ok")
					.data(billDetailsService.getAll(searchDTO))
					.build();
	}
}
