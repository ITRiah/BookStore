package com.example.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.BillDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.BillService;

@RestController
@RequestMapping("/member/bill")
public class BillController {
	
	@Autowired
	BillService BillService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody BillDTO BillDTO) {
		BillService.create(BillDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PutMapping("/update-status")
	public ResponseDTO<Void> update(
						@RequestParam int id,
						@RequestParam String status) {
		
		BillService.updateStatus(status, id);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable int id) {
		BillDTO billDTO = BillService.getById(id);
		
		if(!billDTO.getStatus().equals("Chờ duyệt")) {
			return ResponseDTO.<Void>builder()
					.status(200)
					.msg("Status of Bill must be Loading")
					.build();
		}else {
			BillService.delete(id);
			return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
		}
	}
		
	@GetMapping("/")
	public ResponseDTO<Page<BillDTO>> getAll(@RequestBody SearchDTO searchDTO) {
		return ResponseDTO.<Page<BillDTO>>builder()
					.status(200)
					.msg("ok")
					.data(BillService.getAll(searchDTO))
					.build();
	}
	
	@GetMapping("/get-by-user/{id}")
	public ResponseDTO<List<BillDTO>> getAll(@PathVariable int id) {
		
		return ResponseDTO.<List<BillDTO>>builder()
					.status(200)
					.msg("ok")
					.data(BillService.getByUserId(id))
					.build();
	}	
}
