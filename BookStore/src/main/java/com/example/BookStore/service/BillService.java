package com.example.BookStore.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.BillDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Bill;
import com.example.BookStore.entity.BillDetails;
import com.example.BookStore.entity.ProductDetails;
import com.example.BookStore.repo.BillDetailsRepo;
import com.example.BookStore.repo.BillRepo;
import com.example.BookStore.repo.ProductDetailsRepo;
import com.example.BookStore.repo.ProductRepo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

public interface BillService {
	public void create(BillDTO BillDTO);
	public void delete(int id);
	public BillDTO getById(int id);
	public Page<BillDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class BillServiceImpl implements BillService{
		
		@Autowired
		BillRepo BillRepo;
		
		@Autowired
		BillDetailsRepo billDetailsRepo;
		
		@Autowired
		ProductRepo productRepo;
		
		@Autowired
		ProductDetailsRepo productDetailsRepo;

		@Override
		public void create(BillDTO BillDTO) {
			Bill Bill = new ModelMapper().map(BillDTO, Bill.class);
			BillRepo.save(Bill);
		}
		
		@Override
		@org.springframework.transaction.annotation.Transactional
		public void delete(int id) {
			List<BillDetails> billDetails = billDetailsRepo.findByBillId(id);
			
			for (BillDetails b : billDetails) {
				ProductDetails productDetails = productDetailsRepo.getByProductIdColor(b.getProduct().getId(), b.getColor());
				productDetails.setQuantity(productDetails.getQuantity() + b.getQuantity());
				productDetailsRepo.save(productDetails);
			}
			
//			for (BillDetails billDetails2 : billDetails) {
//				billDetailsRepo.delete(billDetails2);
//			}
			
			BillRepo.deleteById(id);
		}

		@Override
		public Page<BillDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Bill> page = BillRepo.findAll(pageRequest);
			
			Page<BillDTO> page2 =  page.map(Bill -> new ModelMapper().map(Bill, BillDTO.class));
			
			return page2;
		}

		@Override
		public BillDTO getById(int id) {
			Bill bill = BillRepo.findById(id).orElseThrow(NoResultException::new);
			BillDTO billDTO =  new ModelMapper().map(bill, BillDTO.class);
			return billDTO;
		}		
	}
}


