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
import com.example.BookStore.dto.RoleDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/role")
public class RoleController {
	
	@Autowired
	RoleService roleService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody RoleDTO roleDTO) {
		roleService.create(roleDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<RoleDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<RoleDTO>>builder()
					.status(200)
					.msg("ok")
					.data(roleService.getAll(searchDTO))
					.build();
	}
}
