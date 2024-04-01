package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.CartDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Cart;
import com.example.BookStore.repo.CartRepo;

public interface CartService {
	public void create(CartDTO CartDTO);
	public Page<CartDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class CartServiceImpl implements CartService{
		
		@Autowired
		CartRepo CartRepo;

		@Override
		public void create(CartDTO CartDTO) {
			Cart Cart = new ModelMapper().map(CartDTO, Cart.class);
			CartRepo.save(Cart);
		}

		@Override
		public Page<CartDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Cart> page = CartRepo.findAll(pageRequest);
			
			Page<CartDTO> page2 =  page.map(Cart -> new ModelMapper().map(Cart, CartDTO.class));
			
			return page2;
		}
		
	}
}


