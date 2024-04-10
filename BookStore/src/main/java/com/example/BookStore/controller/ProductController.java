package com.example.BookStore.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	ProductService productService;
	
	@Value("${upload.folder}product/")
	private String UPLOAD_FOLDER;

	@PostMapping("/")
	public ResponseDTO<Void> create(
				@ModelAttribute 
				@Valid ProductDTO ProductDTO) throws IllegalStateException, IOException {
		
		if(!(new File(UPLOAD_FOLDER).exists())) {
			new File(UPLOAD_FOLDER).mkdirs();
		}
		
		MultipartFile file = ProductDTO.getFile();
		//String imageDirectory = "C:\\Users\\Admin\\Desktop\\BookStore\\BookStore\\images\\product\\";

		if(file != null) {
			String fileName = file.getOriginalFilename();
			String uniqueFileName =  UUID.randomUUID().toString() + "_" + fileName;
			String filePath = UPLOAD_FOLDER + uniqueFileName;
			file.transferTo(new File(filePath));
			ProductDTO.setImage(uniqueFileName);
		}
		
		productService.create(ProductDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PostMapping("/get-by-id/{id}")
	public ResponseDTO<ProductDTO> getById(@PathVariable int id) {
		
		return ResponseDTO.<ProductDTO>builder()
					.status(200)
					.msg("ok")
					.data(productService.getById(id))
					.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute ProductDTO productDTO) throws IllegalStateException, IOException {
	
		if(!(new File(UPLOAD_FOLDER).exists())) {
			new File(UPLOAD_FOLDER).mkdirs();
		}
		MultipartFile file = productDTO.getFile();
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			String uniqueFileName =  UUID.randomUUID().toString() + "_" + fileName;
			String filePath = UPLOAD_FOLDER + uniqueFileName;
			file.transferTo(new File(filePath));
			productDTO.setImage(uniqueFileName);
		}
		
		productService.update(productDTO);
		
		return ResponseDTO.<Void>builder()
			.status(200)
			.msg("update success")
			.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<ProductDTO>> getAll(@RequestBody SearchDTO searchDTO) {
		return ResponseDTO.<Page<ProductDTO>>builder()
					.status(200)
					.msg("ok")
					.data(productService.getAll(searchDTO))
					.build();
	}
	
	@PostMapping("/search-by-name")
	public ResponseDTO<Page<ProductDTO>> searchByName(@RequestBody SearchDTO searchDTO) {
		System.out.println(searchDTO.getKeyword());
		
		return ResponseDTO.<Page<ProductDTO>>builder()
				.status(200)
				.msg("ok")
				.data(productService.searchByName(searchDTO))
				.build();
	}
	
	
	@GetMapping("/download")
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		File file = new File(UPLOAD_FOLDER + fileName);
		Files.copy(file.toPath(), response.getOutputStream());// lấy dữ liệu từ file để tải về hình ảnh cho web
	}
}
