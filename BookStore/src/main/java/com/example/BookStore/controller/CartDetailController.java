package com.example.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.dto.CartDetailDTO;
import com.example.BookStore.dto.ProductDetailDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.service.CartDetailService;
import com.example.BookStore.service.ProductDetailService;

@RestController
@RequestMapping("/member/cart-details")
public class CartDetailController {
	
	@Autowired
	CartDetailService cartDetailsService;
	
	@Autowired
	ProductDetailService detailsService;

	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody CartDetailDTO CartDetailsDTO) {
		String color = CartDetailsDTO.getColor();
		int productId = CartDetailsDTO.getProduct().getId();
		
		
		ProductDetailDTO productDetailsDTO = detailsService.findByProductIdAndColor(productId, color);
		
		if(productDetailsDTO != null) {
			if(productDetailsDTO.getQuantity() < CartDetailsDTO.getQuantity()) {
				return ResponseDTO.<Void>builder()
						.status(200)
						.msg("Quantity is not enough")
						.build();
			}
		}
		
		cartDetailsService.create(CartDetailsDTO);
		return ResponseDTO.<Void>builder()
					.status(200)
					.msg("ok")
					.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody CartDetailDTO cartDetailsDTO){
		cartDetailsService.update(cartDetailsDTO);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build(); 
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> update(@PathVariable int id){
		cartDetailsService.delete(id);
		
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build(); 
	}
	
	
	@GetMapping("/")
	public ResponseDTO<Page<CartDetailDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<CartDetailDTO>>builder()
					.status(200)
					.msg("ok")
					.data(cartDetailsService.getAll(searchDTO))
					.build();
	}
}
