package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.BillDetailDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Bill;
import com.example.BookStore.entity.BillDetail;
import com.example.BookStore.entity.Cart;
import com.example.BookStore.entity.CartDetail;
import com.example.BookStore.entity.ProductDetail;
import com.example.BookStore.repo.BillDetailRepo;
import com.example.BookStore.repo.BillRepo;
import com.example.BookStore.repo.CartDetailRepo;
import com.example.BookStore.repo.CartRepo;
import com.example.BookStore.repo.ProductDetailRepo;

public interface BillDetailService {
	public void create(BillDetailDTO BillDetailsDTO);
	public Page<BillDetailDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class BillDetailsServiceImpl implements BillDetailService{
		
		@Autowired
		BillDetailRepo billDetailsRepo;
		
		@Autowired
		BillRepo billRepo;
		
		@Autowired
		ProductDetailRepo productDetailsRepo;
				
		@Autowired
		CartDetailRepo cartDetailsRepo;
		
		@Autowired
		CartDetailService cartDetailsService;
		
		@Override
		public void create(BillDetailDTO BillDetailsDTO) {
			
			//update product
			int productId = BillDetailsDTO.getProduct().getId();
			String color = BillDetailsDTO.getColor();
						
			ProductDetail productDetails = productDetailsRepo.getByProductIdColor(productId, color);
			productDetails.setQuantity(productDetails.getQuantity() - BillDetailsDTO.getQuantity());
			productDetailsRepo.save(productDetails);
			
			//update billDetails
			BillDetailsDTO.setTotalPrice(BillDetailsDTO.getQuantity() * productDetails.getPrice());
			BillDetail BillDetails = new ModelMapper().map(BillDetailsDTO, BillDetail.class);
			billDetailsRepo.save(BillDetails);	
						
			//update bill
			int billId = BillDetailsDTO.getBill().getId();
			Bill bill = billRepo.getById(billId);
			bill.setTotalPrice(bill.getTotalPrice() + BillDetailsDTO.getTotalPrice());
			billRepo.save(bill);
			
			//delete cartDetails
			CartDetail cartDetails = cartDetailsRepo.getByProductIdAndColor(productId, color);
			cartDetailsService.delete(cartDetails.getId());
		}

		@Override
		public Page<BillDetailDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<BillDetail> page = billDetailsRepo.findAll(pageRequest);
			
			Page<BillDetailDTO> page2 =  page.map(BillDetails -> new ModelMapper().map(BillDetails, BillDetailDTO.class));
			
			return page2;
		}
	}
}


