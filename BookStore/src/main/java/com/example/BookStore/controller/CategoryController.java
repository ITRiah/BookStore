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

import com.example.BookStore.dto.CategoryDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody @Valid CategoryDTO CategoryDTO) {
		
		categoryService.create(CategoryDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody CategoryDTO categoryDTO) {
		categoryService.update(categoryDTO);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();
	}
	
	@GetMapping("/get-by-id/{id}")
	public ResponseDTO<CategoryDTO> getById(@PathVariable int id) {
		return ResponseDTO.<CategoryDTO>builder()
					.status(200)
					.msg("ok")
					.data(categoryService.getById(id))
					.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable int id){
		categoryService.delete(id);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<CategoryDTO>> getAll(@RequestBody SearchDTO searchDTO) {
		return ResponseDTO.<Page<CategoryDTO>>builder()
					.status(200)
					.msg("ok")
					.data(categoryService.getAll(searchDTO))
					.build();
	}
}
