package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.BillDetailsDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Bill;
import com.example.BookStore.entity.BillDetails;
import com.example.BookStore.entity.ProductDetails;
import com.example.BookStore.repo.BillDetailsRepo;
import com.example.BookStore.repo.BillRepo;
import com.example.BookStore.repo.CartDetailsRepo;
import com.example.BookStore.repo.ProductDetailsRepo;

public interface BillDetailsService {
	public void create(BillDetailsDTO BillDetailsDTO);
	public Page<BillDetailsDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class BillDetailsServiceImpl implements BillDetailsService{
		
		@Autowired
		BillDetailsRepo BillDetailsRepo;
		
		@Autowired
		BillRepo billRepo;
		
		@Autowired
		ProductDetailsRepo productDetailsRepo;
				
		@Autowired
		CartDetailsRepo cartDetailsRepo;

		@Override
		public void create(BillDetailsDTO BillDetailsDTO) {
			
			//update product
			int productId = BillDetailsDTO.getProduct().getId();
			String color = BillDetailsDTO.getColor();
			
			ProductDetails productDetails = productDetailsRepo.getByProductIdColor(productId, color);
			productDetails.setQuantity(productDetails.getQuantity() - BillDetailsDTO.getQuantity());
			productDetailsRepo.save(productDetails);
			
			//update billDetails
			BillDetailsDTO.setTotalPrice(BillDetailsDTO.getQuantity() * productDetails.getPrice());
			BillDetails BillDetails = new ModelMapper().map(BillDetailsDTO, BillDetails.class);
			BillDetailsRepo.save(BillDetails);	
			
			System.out.println("billDetails: " + BillDetailsDTO.getQuantity() * productDetails.getPrice());
			
			//update bill
			int billId = BillDetailsDTO.getBill().getId();
			Bill bill = billRepo.getById(billId);
			bill.setTotalPrice(bill.getTotalPrice() + BillDetailsDTO.getTotalPrice());
			billRepo.save(bill);
			
			System.out.println("bill" + bill.getTotalPrice());

			
			
		}

		@Override
		public Page<BillDetailsDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<BillDetails> page = BillDetailsRepo.findAll(pageRequest);
			
			Page<BillDetailsDTO> page2 =  page.map(BillDetails -> new ModelMapper().map(BillDetails, BillDetailsDTO.class));
			
			return page2;
		}
		
	}
}


