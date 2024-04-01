package com.example.BookStore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
	private int id;
	
	@NotBlank
	private String name;
}
