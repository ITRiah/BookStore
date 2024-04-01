package com.example.BookStore.dto;

import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class BillDetailsDTO {
	private int id;
	
	private int quantity;
	private String color;
	private double totalPrice;
	
	@ManyToOne
	private BillDTO bill;
	
	@ManyToOne
	private ProductDTO product;
}
