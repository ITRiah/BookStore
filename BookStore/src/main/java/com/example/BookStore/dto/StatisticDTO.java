package com.example.BookStore.dto;

import java.util.Date;

import lombok.Data;

@Data
public class StatisticDTO {
	private String keyword;
	private Integer currentPage;
	private Integer size;	
	private String sortedField;
	private Date from;
	private Date to;
}
