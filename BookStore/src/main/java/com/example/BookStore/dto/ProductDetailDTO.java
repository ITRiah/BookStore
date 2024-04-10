package com.example.BookStore.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ProductDetailDTO {
	private int id;
	
	private String color;
	private String image;
	private double price;
	private int quantity;
	
	@JsonIgnore
	private MultipartFile file;
	
	
	private ProductDTO product;
}
