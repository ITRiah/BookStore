package com.example.BookStore.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookStore.dto.ProductDetailDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.repo.ProductRepo;
import com.example.BookStore.service.ProductDetailService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/product-details")
public class ProductDetailController {

	@Autowired
	ProductDetailService productDetailsService;
	
	@Autowired
	ProductRepo productRepo;
	
	@Value("${upload.folder}product/")
	private String UPLOAD_FOLDER;

	@PostMapping("/")
	public ResponseDTO<Void> create(@ModelAttribute ProductDetailDTO ProductDetailsDTO) throws IllegalStateException, IOException {
		if(!(new File(UPLOAD_FOLDER).exists())) {
			new File(UPLOAD_FOLDER).mkdirs();
		}
		
		MultipartFile file = ProductDetailsDTO.getFile();		
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
			String filePath = (	UPLOAD_FOLDER + fileName);

			file.transferTo(new File(filePath));
			ProductDetailsDTO.setImage(uniqueFileName);
		}
		
		productDetailsService.create(ProductDetailsDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();

	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute ProductDetailDTO ProductDetailsDTO) throws IllegalStateException, IOException {
		if(!(new File(UPLOAD_FOLDER).exists())) {
			new File(UPLOAD_FOLDER).mkdirs();
		}
		
		MultipartFile file = ProductDetailsDTO.getFile();		
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
			String filePath = (	UPLOAD_FOLDER + fileName);
			
			//save file
			file.transferTo(new File(filePath));
			
			//set image
			ProductDetailsDTO.setImage(uniqueFileName);
		}
		
		productDetailsService.update(ProductDetailsDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();

	}
	@GetMapping("/")
	public ResponseDTO<Page<ProductDetailDTO>> getAll(@RequestBody SearchDTO searchDTO) {
		return ResponseDTO.<Page<ProductDetailDTO>>builder().status(200).msg("ok")
				.data(productDetailsService.getAll(searchDTO)).build();
	}
	
	@GetMapping("/get-by-product-id")
	public ResponseDTO<List<ProductDetailDTO>> getByProductId(@RequestParam("id") int id) {
		return ResponseDTO.<List<ProductDetailDTO>>builder()
				.status(200)
				.msg("ok")
				.data(productDetailsService.findByProductId(id))
				.build();
	}


	@GetMapping("/download")
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		File file = new File(UPLOAD_FOLDER + fileName);
		Files.copy(file.toPath(), response.getOutputStream());// lấy dữ liệu từ file để tải về hình ảnh cho web
	}
}
