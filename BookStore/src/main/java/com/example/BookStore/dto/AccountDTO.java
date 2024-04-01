package com.example.BookStore.dto;

import java.util.Date;
import java.util.List;

import com.example.BookStore.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountDTO {
	private int id;
	
	@NotBlank
	@Column(unique = true)
	private String userName;
	
	@Min(6)
	private String passWord;
	private String status;
	
	private List<Role> roles;
	
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date createAt;
}
