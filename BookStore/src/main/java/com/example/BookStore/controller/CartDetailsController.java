package com.example.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.CartDetailsDTO;
import com.example.BookStore.dto.ProductDetailsDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.CartDetailsService;
import com.example.BookStore.service.ProductDetailsService;

@RestController
@RequestMapping("/member/cart-details")
public class CartDetailsController {
	
	@Autowired
	CartDetailsService CartDetailsService;
	
	@Autowired
	ProductDetailsService detailsService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody CartDetailsDTO CartDetailsDTO) {
		String color = CartDetailsDTO.getColor();
		
		List<ProductDetailsDTO> detailsDTOs = detailsService.findByProductId(CartDetailsDTO.getProduct().getId());
		
		for (ProductDetailsDTO productDetailsDTO : detailsDTOs) {
			if(productDetailsDTO.getColor().equals(color)) {
				if(CartDetailsDTO.getQuantity() > productDetailsDTO.getQuantity()) {
					return ResponseDTO.<Void>builder()
							.status(200)
							.msg("Số lượng sản phẩm không đủ!")
							.build();
				}
			}
		}
		
		CartDetailsService.create(CartDetailsDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<Page<CartDetailsDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<CartDetailsDTO>>builder()
					.status(200)
					.msg("ok")
					.data(CartDetailsService.getAll(searchDTO))
					.build();
	}
}
