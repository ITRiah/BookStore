package com.example.BookStore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BookStore.dto.BillDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.dto.StatisticDTO;
import com.example.BookStore.entity.Bill;
import com.example.BookStore.entity.BillDetail;
import com.example.BookStore.entity.ProductDetail;
import com.example.BookStore.repo.BillDetailRepo;
import com.example.BookStore.repo.BillRepo;
import com.example.BookStore.repo.ProductDetailRepo;
import com.example.BookStore.repo.ProductRepo;

import jakarta.persistence.NoResultException;

public interface BillService {
	public void create(BillDTO BillDTO);

	public void delete(int id);
	
	public void updateStatus(String status, int id);

	public List<BillDTO> getByUserId(int id);

	public BillDTO getById(int id);

	public Page<BillDTO> getAll(SearchDTO searchDTO);

	public Page<BillDTO> getByStatus(SearchDTO searchDTO, String status);

	public Page<BillDTO> getByDate(StatisticDTO statisticDTO);
	
	@Service
	public class BillServiceImpl implements BillService {

		@Autowired
		BillRepo billRepo;

		@Autowired
		BillDetailRepo billDetailRepo;

		@Autowired
		ProductRepo productRepo;

		@Autowired
		ProductDetailRepo productDetailsRepo;

		@Override
		public void create(BillDTO BillDTO) {
			Bill Bill = new ModelMapper().map(BillDTO, Bill.class);
			billRepo.save(Bill);
		}
		
		@Override
		public void updateStatus(String status, int id) {
			Bill billCurrent = billRepo.getById(id);
			if(billCurrent != null) {
				billCurrent.setStatus(status);
				billRepo.save(billCurrent);
			}
		}

		@Override
		@Transactional
		public void delete(int id) {
			List<BillDetail> billDetails = billDetailRepo.findByBillId(id);

			// update product (quantity)
			for (BillDetail b : billDetails) {
				ProductDetail productDetails = productDetailsRepo.getByProductIdColor(b.getProduct().getId(),
						b.getColor());
				productDetails.setQuantity(productDetails.getQuantity() + b.getQuantity());
				productDetailsRepo.save(productDetails);
			}

//			for (BillDetails billDetails2 : billDetails) {
//				billDetailsRepo.delete(billDetails2);
//			}

			billRepo.deleteById(id);
		}

		@Override
		public Page<BillDTO> getAll(SearchDTO searchDTO) {

			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage();
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();

			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();

			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Bill> page = billRepo.findAll(pageRequest);

			Page<BillDTO> page2 = page.map(Bill -> new ModelMapper().map(Bill, BillDTO.class));

			return page2;
		}

		@Override
		public Page<BillDTO> getByStatus(SearchDTO searchDTO, String status) {

			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage();
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();

			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();

			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Bill> page = billRepo.findByStatus(status, pageRequest);

			Page<BillDTO> page2 = page.map(Bill -> new ModelMapper().map(Bill, BillDTO.class));

			return page2;
		}

		@Override
		public BillDTO getById(int id) {
			Bill bill = billRepo.findById(id).orElseThrow(NoResultException::new);
			BillDTO billDTO = new ModelMapper().map(bill, BillDTO.class);
			return billDTO;
		}

		@Override
		public List<BillDTO> getByUserId(int id) {
			List<Bill> bills = billRepo.getByUserId(id);

			List<BillDTO> billDTOs = bills.stream().map(bill -> new ModelMapper().map(bill, BillDTO.class))
					.collect(Collectors.toList());
			return billDTOs;
		}

		@Override
		public Page<BillDTO> getByDate(StatisticDTO statisticDTO) {
			int currentPage = statisticDTO.getCurrentPage() == null ? 0 : statisticDTO.getCurrentPage();
			int size = statisticDTO.getSize() == null ? 5 : statisticDTO.getSize();

			String sortField = statisticDTO.getSortedField() == null ? "id" : statisticDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();

			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Bill> page = billRepo.searchByDate(statisticDTO.getFrom(), statisticDTO.getTo(), pageRequest);

			Page<BillDTO> page2 = page.map(Bill -> new ModelMapper().map(Bill, BillDTO.class));

			return page2;
		}		
	}
}
