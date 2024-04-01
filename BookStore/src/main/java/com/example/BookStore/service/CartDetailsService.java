package com.example.BookStore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.CartDetailsDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Cart;
import com.example.BookStore.entity.CartDetails;
import com.example.BookStore.repo.CartDetailsRepo;
import com.example.BookStore.repo.CartRepo;

public interface CartDetailsService {
	public void create(CartDetailsDTO CartDetailsDTO);
	public Page<CartDetailsDTO> getAll(SearchDTO searchDTO);
	public List<CartDetailsDTO> findByProductId(int productId);
	public void delete(int id);

	
	@Service
	public class CartDetailsServiceImpl implements CartDetailsService{
		
		@Autowired
		CartDetailsRepo CartDetailsRepo;
		
		@Autowired
		CartRepo cartRepo;
		
		@Autowired
		CartDetailsRepo cartDetailsRepo;
		
		@Override
		public void create(CartDetailsDTO CartDetailsDTO) {
			int cartId = CartDetailsDTO.getCart().getId();
			Cart cart = cartRepo.getById(cartId);
			
			//update totalAmount
			double totalAmount = CartDetailsDTO.getProduct().getPrice() * CartDetailsDTO.getQuantity();
			CartDetailsDTO.setTotalAmount(totalAmount);
			
			//update cart
			cart.setTotalQuantity(cart.getTotalQuantity() + CartDetailsDTO.getQuantity());
			cart.setTotalPrice(cart.getTotalPrice() + totalAmount);
			
			CartDetails CartDetails = new ModelMapper().map(CartDetailsDTO, CartDetails.class);
			CartDetailsRepo.save(CartDetails);
			cartRepo.save(cart);
		}

		@Override
		public Page<CartDetailsDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<CartDetails> page = CartDetailsRepo.findAll(pageRequest);
			
			Page<CartDetailsDTO> page2 =  page.map(CartDetails -> new ModelMapper().map(CartDetails, CartDetailsDTO.class));
			
			return page2;
		}
		
		@Override
		public List<CartDetailsDTO> findByProductId(int productId) {
			List<CartDetails> details = cartDetailsRepo.getByProductId(productId);

			List<CartDetailsDTO> detailsDTOs = details.stream().map(
					cartDetails -> new ModelMapper().map(cartDetails, CartDetailsDTO.class)
			).collect(Collectors.toList());
			
			return detailsDTOs;
		}

		@Override
		public void delete(int id) {
			CartDetailsRepo.deleteById(id);
		}
		
	}
}


