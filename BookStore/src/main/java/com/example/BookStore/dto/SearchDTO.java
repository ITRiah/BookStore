package com.example.BookStore.dto;

import lombok.Data;

@Data
public class SearchDTO {
	private String keyword;
	private Integer currentPage;
	private Integer size;	
	private String sortedField;
}
