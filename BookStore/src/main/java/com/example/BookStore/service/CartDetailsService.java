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
import com.example.BookStore.entity.Product;
import com.example.BookStore.repo.CartDetailsRepo;
import com.example.BookStore.repo.CartRepo;
import com.example.BookStore.repo.ProductRepo;

public interface CartDetailsService {
	public void create(CartDetailsDTO CartDetailsDTO);
	public void update(CartDetailsDTO CartDetailsDTO);
	public void delete(int id);

	public Page<CartDetailsDTO> getAll(SearchDTO searchDTO);
	public List<CartDetailsDTO> findByProductId(int productId);

	
	@Service
	public class CartDetailsServiceImpl implements CartDetailsService{
		
		@Autowired
		CartDetailsRepo CartDetailsRepo;
		
		@Autowired
		CartRepo cartRepo;
		
		@Autowired
		ProductRepo productRepo;
		
		@Autowired
		CartDetailsRepo cartDetailsRepo;
		
		@Override
		public void create(CartDetailsDTO CartDetailsDTO) {
			//Nếu sản phẩm đã tồn tại thì là số lượng thêm mới, còn không là số lượng được khởi tạo ban đầu
			int extraQuantity = CartDetailsDTO.getQuantity();
			
			CartDetails cartDetails = new CartDetails();
			cartDetails = cartDetailsRepo.getByProductIdAndColor(CartDetailsDTO.getProduct().getId(), CartDetailsDTO.getColor());			
			
			//Nếu exist sản phẩm -> tăng số lượng
			if(cartDetails != null) {
				cartDetails.setQuantity(cartDetails.getQuantity() + extraQuantity);
			}else {
				cartDetails = new ModelMapper().map(CartDetailsDTO, CartDetails.class);
			}			
			
			//Get cart
			int cartId = cartDetails.getCart().getId();
			Cart cart = cartRepo.getById(cartId);
			
			//Get product vì khi lấy product qua cartdetails chỉ có id
			Product product = productRepo.getById(cartDetails.getProduct().getId());

			//update totalAmount
			double totalAmount = product.getPrice() * cartDetails.getQuantity();
			cartDetails.setTotalAmount(totalAmount);
			
			//save to cartDetails
			CartDetailsRepo.save(cartDetails);
			
			//update cart
			//Chỉ cộng thêm lượng mới
			cart.setTotalQuantity(cart.getTotalQuantity() + extraQuantity);
			cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * extraQuantity);
			
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
		public void update(CartDetailsDTO CartDetailsDTO) {
			int quantityUpdate = CartDetailsDTO.getQuantity();
			int quantityCurrent = 0;
			int idCartDetails = CartDetailsDTO.getId();
			
			//update quantity
			CartDetails cartDetails = cartDetailsRepo.getById(idCartDetails);
			quantityCurrent = cartDetails.getQuantity();
			cartDetails.setQuantity(quantityUpdate);
			
			//Get product vì khi lấy product qua cartdetails chỉ có id
			Product product = productRepo.getById(cartDetails.getProduct().getId());
			
			//update totalAmount
			double totalAmount = product.getPrice() * cartDetails.getQuantity();
			CartDetailsDTO.setTotalAmount(totalAmount);
			
			//save to cartDetails
			CartDetailsRepo.save(cartDetails);
			
			//Get cart by cardId
			int cartId = cartDetails.getCart().getId();
			Cart cart = cartRepo.getById(cartId);
			
			//update cart
			//if change > 0 ->  cộng , change < 0 -> trừ
			int quantityChange = quantityUpdate - quantityCurrent;
			
			if(quantityChange > 0) {
				cart.setTotalQuantity(cart.getTotalQuantity() + quantityChange);
				cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * quantityChange);	
			}else {
				cart.setTotalQuantity(cart.getTotalQuantity() + quantityChange);
				cart.setTotalPrice(cart.getTotalPrice() - product.getPrice() * (-quantityChange));	
			}
			
			//save to cart
			cartRepo.save(cart);
		}

		@Override
		public void delete(int id) {
			
			//get CartDetails
			CartDetails cartDetails = cartDetailsRepo.getById(id);
			
			//Get cart
			int cartId = cartDetails.getCart().getId();
			Cart cart = cartRepo.getById(cartId);
			
			//update cart
			cart.setTotalQuantity(cart.getTotalQuantity() - cartDetails.getQuantity());
			
			Product product = productRepo.getById(cartDetails.getProduct().getId());
			double totalAmount = product.getPrice() * cartDetails.getQuantity();
			cart.setTotalPrice(cart.getTotalPrice() - totalAmount);	
			
			//delete cartDetails
			cartDetailsRepo.deleteById(id);
		
			//save to cart
			cartRepo.save(cart);
		}



		
		
	}
}


