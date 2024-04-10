package com.example.BookStore.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class BillDTO{
	private int id;
	
	private double totalPrice;
	private String status;
	
	private UserDTO user;
	
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date createAt;
	
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date receiveAt;
	
	@JsonIgnoreProperties("bill") 
	private List<BillDetailDTO> billDetails;
}
