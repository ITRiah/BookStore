package com.example.BookStore.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.BillDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.dto.StatisticDTO;
import com.example.BookStore.service.BillService;

@RestController
@RequestMapping("/admin/statistic")
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
	
	@PostMapping("/get-by-date")
	public ResponseDTO<Page<BillDTO>> statisticbyDate(
			@RequestBody StatisticDTO statisticDTO) {

		Page<BillDTO> page = billService.getByDate(statisticDTO);

		return ResponseDTO.<Page<BillDTO>>builder()
					.status(200)
					.msg("ok")
					.data(page)
					.build();
	}
}
