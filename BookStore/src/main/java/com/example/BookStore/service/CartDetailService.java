package com.example.BookStore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.CartDetailDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Cart;
import com.example.BookStore.entity.CartDetail;
import com.example.BookStore.entity.Product;
import com.example.BookStore.repo.CartDetailRepo;
import com.example.BookStore.repo.CartRepo;
import com.example.BookStore.repo.ProductRepo;

public interface CartDetailService {
	public void create(CartDetailDTO CartDetailsDTO);
	public void update(CartDetailDTO CartDetailsDTO);
	public void delete(int id);
	public CartDetailDTO findByProductIdAndColor(int id, String color);

	public Page<CartDetailDTO> getAll(SearchDTO searchDTO);
	public List<CartDetailDTO> findByProductId(int productId);

	
	@Service
	public class CartDetailsServiceImpl implements CartDetailService{
		
		@Autowired
		CartDetailRepo cartDetailsRepo;
		
		@Autowired
		CartRepo cartRepo;
		
		@Autowired
		ProductRepo productRepo;
		
		@Override
		public void create(CartDetailDTO CartDetailsDTO) {
			//Nếu sản phẩm đã tồn tại thì là số lượng thêm mới, còn không là số lượng được khởi tạo ban đầu
			int extraQuantity = CartDetailsDTO.getQuantity();
			
			CartDetail cartDetails = new CartDetail();
			cartDetails = cartDetailsRepo.getByProductIdAndColor(CartDetailsDTO.getProduct().getId(), CartDetailsDTO.getColor());			
			
			//Nếu exist sản phẩm -> tăng số lượng
			if(cartDetails != null) {
				cartDetails.setQuantity(cartDetails.getQuantity() + extraQuantity);
			}else {
				cartDetails = new ModelMapper().map(CartDetailsDTO, CartDetail.class);
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
			cartDetailsRepo.save(cartDetails);
			
			//update cart
			//Chỉ cộng thêm lượng mới
			cart.setTotalQuantity(cart.getTotalQuantity() + extraQuantity);
			cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * extraQuantity);
			
			cartRepo.save(cart);
		}
		
		

		@Override
		public Page<CartDetailDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<CartDetail> page = cartDetailsRepo.findAll(pageRequest);
			
			Page<CartDetailDTO> page2 =  page.map(CartDetails -> new ModelMapper().map(CartDetails, CartDetailDTO.class));
			
			return page2;
		}
		
		@Override
		public List<CartDetailDTO> findByProductId(int productId) {
			List<CartDetail> details = cartDetailsRepo.getByProductId(productId);

			List<CartDetailDTO> detailsDTOs = details.stream().map(
					cartDetails -> new ModelMapper().map(cartDetails, CartDetailDTO.class)
			).collect(Collectors.toList());
			
			return detailsDTOs;
		}
		
		@Override
		public void update(CartDetailDTO CartDetailsDTO) {
			int quantityUpdate = CartDetailsDTO.getQuantity();
			int quantityCurrent = 0;
			int idCartDetails = CartDetailsDTO.getId();
			
			//update quantity
			CartDetail cartDetails = cartDetailsRepo.getById(idCartDetails);
			quantityCurrent = cartDetails.getQuantity();
			cartDetails.setQuantity(quantityUpdate);
			
			//Get product vì khi lấy product qua cartdetails chỉ có id
			Product product = productRepo.getById(cartDetails.getProduct().getId());
			
			//update totalAmount
			double totalAmount = product.getPrice() * cartDetails.getQuantity();
			CartDetailsDTO.setTotalAmount(totalAmount);
			
			//save to cartDetails
			cartDetailsRepo.save(cartDetails);
			
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
			CartDetail cartDetails = cartDetailsRepo.getById(id);
			
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


		@Override
		public CartDetailDTO findByProductIdAndColor(int id, String color) {
			CartDetail cartDetails = cartDetailsRepo.getByProductIdAndColor(id, color);
			CartDetailDTO cartDetailsDTO =  new ModelMapper().map(cartDetails, CartDetailDTO.class);
			return cartDetailsDTO;
		}
	}
}


