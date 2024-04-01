package com.example.BookStore.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CartDTO{
	private int id;
	
	private double totalPrice;
	private int totalQuantity;
	
	private UserDTO user;
	
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date createAt;
}
