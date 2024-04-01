package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.BillDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Bill;
import com.example.BookStore.repo.BillRepo;

public interface BillService {
	public void create(BillDTO BillDTO);
	public Page<BillDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class BillServiceImpl implements BillService{
		
		@Autowired
		BillRepo BillRepo;

		@Override
		public void create(BillDTO BillDTO) {
			Bill Bill = new ModelMapper().map(BillDTO, Bill.class);
			BillRepo.save(Bill);
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
		
	}
}


