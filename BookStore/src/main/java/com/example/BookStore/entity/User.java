package com.example.BookStore.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id;
	private String email;
	private String name;
	private String avatar;
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	private String address;
	
	@OneToOne
	private Account account;
	
}
