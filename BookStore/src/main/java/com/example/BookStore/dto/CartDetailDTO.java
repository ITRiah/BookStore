package com.example.BookStore.dto;

import lombok.Data;

@Data
public class CartDetailDTO {
	
	private int id;
	
	private String color;
	private int quantity;
	
	//Thanh tien
	private double totalAmount;
	
	private ProductDTO product;
	
	private CartDTO cart;
}
