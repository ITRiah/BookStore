package com.example.BookStore.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtSerivce {

	@Value("${jwt.secret : 6868688686868686886868686868686868686868688686868686868686886}")
	private String secretKey;
	private long validity = 5; // 5 phút

	public String createToken(String username, String password) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("pw", password);
		
		Date now = new Date();
		Date exp = new Date(now.getTime() + validity * 60 * 1000);

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	public String getUserName(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
}
