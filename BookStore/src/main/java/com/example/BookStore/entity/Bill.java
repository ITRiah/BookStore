package com.example.BookStore.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Bill extends TimeAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private double totalPrice;
	private String status;
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy = "bill", cascade = CascadeType.REMOVE)// mappedBy
	private List<BillDetail> billDetails;
	
	@LastModifiedDate
	private Date receiveAt; //
}
