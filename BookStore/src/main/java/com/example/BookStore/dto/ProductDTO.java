package com.example.BookStore.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDTO {
	private int id;
	
	@NotBlank
	private String name;
	private String image;
	private double price;
	private int quantity;
	private String description;
	private String status;
	
	@JsonIgnore
	private MultipartFile file;
	
	private CategoryDTO category;
}
