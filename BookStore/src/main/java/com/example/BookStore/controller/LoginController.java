package com.example.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.service.JwtSerivce;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtSerivce jwtSerivce;
	
	
	@PostMapping("/")
	public ResponseDTO<String> login(@RequestParam("username") String username, @RequestParam("password") String password){
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		return ResponseDTO.<String>builder()
				.msg("login success")
				.data(jwtSerivce.createToken(username, password))
				.status(200)
				.build();
	}
}
