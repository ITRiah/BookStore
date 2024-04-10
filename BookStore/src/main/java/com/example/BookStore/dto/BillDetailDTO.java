package com.example.BookStore.dto;

import lombok.Data;

@Data
public class BillDetailDTO {
	private int id;
	
	private int quantity;
	private String color;
	private double totalPrice;
	
	private BillDTO bill;
	
	private ProductDTO product;
}
