package com.example.BookStore.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDTO {
	private int id;
	private String email;
	private String name;
	
	@JsonIgnore
	private String avatar;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private String birthdate;
	private String address;
	
	@JsonIgnore
	private MultipartFile file;
	
	private AccountDTO account;
	
}
