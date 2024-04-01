package com.example.BookStore.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookStore.dto.ProductDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/product")
public class ProductController {
	
	@Autowired
	ProductService ProductService;

	@PostMapping("/")
	public ResponseDTO<Void> create(
				@ModelAttribute 
				@Valid ProductDTO ProductDTO) throws IllegalStateException, IOException {
		MultipartFile file = ProductDTO.getFile();
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			String uniqueFileName =  UUID.randomUUID().toString() + "_" + fileName;
			String filePath = "E:\\Studying\\Java Backend\\Springboot\\BookStore\\src\\main\\java\\com\\example\\BookStore\\image\\product\\"
					+ uniqueFileName;
			file.transferTo(new File(filePath));
			ProductDTO.setImage(filePath);
		}
		
		ProductService.create(ProductDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<ProductDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<ProductDTO>>builder()
					.status(200)
					.msg("ok")
					.data(ProductService.getAll(searchDTO))
					.build();
	}
	
	
	@GetMapping("/download")
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		File file = new File("E:\\Studying\\Java Backend\\Springboot\\BookStore\\src\\main\\java\\com\\example\\BookStore\\image\\product\\" + fileName);
		Files.copy(file.toPath(), response.getOutputStream());// lấy dữ liệu từ file để tải về hình ảnh cho web
	}
}
