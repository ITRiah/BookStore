package com.example.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.BillDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.BillService;

@RestController
@RequestMapping("/statistic")
public class StatisticsController {
	@Autowired
	BillService billService;
	
	@GetMapping("/")
	public ResponseDTO<Page<BillDTO>> statistic(@RequestBody SearchDTO searchDTO) {
		
		Page<BillDTO> page = billService.getByStatus(searchDTO, "Hoàn thành");
		
		return ResponseDTO.<Page<BillDTO>>builder()
					.status(200)
					.msg("ok")
					.data(page)
					.build();
	}
}
