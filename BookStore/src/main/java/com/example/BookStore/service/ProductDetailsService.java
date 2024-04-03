package com.example.BookStore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.ProductDetailsDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.ProductDetails;
import com.example.BookStore.repo.ProductDetailsRepo;

public interface ProductDetailsService {
	public void create(ProductDetailsDTO ProductDetailsDTO);
	public Page<ProductDetailsDTO> getAll(SearchDTO searchDTO);
	public List<ProductDetailsDTO> findByProductId(int productId);
	public ProductDetailsDTO findByProductIdAndColor(int productId , String color);
	
	@Service
	public class ProductDetailsServiceImpl implements ProductDetailsService{
		
		@Autowired
		ProductDetailsRepo ProductDetailsRepo;

		@Override
		public void create(ProductDetailsDTO ProductDetailsDTO) {
			ProductDetails ProductDetails = new ModelMapper().map(ProductDetailsDTO, ProductDetails.class);
			ProductDetailsRepo.save(ProductDetails);
		}

		@Override
		public Page<ProductDetailsDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<ProductDetails> page = ProductDetailsRepo.findAll(pageRequest);
			
			Page<ProductDetailsDTO> page2 =  page.map(ProductDetails -> new ModelMapper().map(ProductDetails, ProductDetailsDTO.class));
			
			return page2;
		}

		@Override
		public List<ProductDetailsDTO> findByProductId(int productId) {
			List<ProductDetails> details = ProductDetailsRepo.getByProductId(productId);

			List<ProductDetailsDTO> detailsDTOs = details.stream().map(
					productDetails -> new ModelMapper().map(productDetails, ProductDetailsDTO.class)
			).collect(Collectors.toList());
			
			return detailsDTOs;
		}

		@Override
		public ProductDetailsDTO findByProductIdAndColor(int productId, String color) {
			ProductDetails productDetails = ProductDetailsRepo.getByProductIdColor(productId, color);
			
			ProductDetailsDTO productDetailsDTO = new ModelMapper().map(productDetails, ProductDetailsDTO.class);
			
			return productDetailsDTO;
		}
	}
}


