package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.AccountDTO;
import com.example.BookStore.dto.ProductDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Account;
import com.example.BookStore.entity.Product;
import com.example.BookStore.repo.ProductRepo;

import jakarta.persistence.NoResultException;

public interface ProductService {
	public void create(ProductDTO ProductDTO);
	public void update(ProductDTO productDTO);
	public ProductDTO getById(int id);
	public Page<ProductDTO> getAll(SearchDTO searchDTO);
	public Page<ProductDTO> searchByName(SearchDTO searchDTO);

	
	@Service
	public class ProductServiceImpl implements ProductService{
		
		@Autowired
		ProductRepo ProductRepo;

		@Override
		public void create(ProductDTO ProductDTO) {
			Product Product = new ModelMapper().map(ProductDTO, Product.class);
			ProductRepo.save(Product);
		}
		
		@Override
		public void update(ProductDTO productDTO) {
			Product productCurrent = ProductRepo.getById(productDTO.getId());
			productCurrent = new ModelMapper().map(productDTO, Product.class);
			ProductRepo.save(productCurrent);
		}
		
		@Override
		public ProductDTO getById(int id) {
			Product product = ProductRepo.findById(id).orElseThrow(NoResultException::new);
			ProductDTO productDTO = new ModelMapper().map(product, ProductDTO.class);
			return productDTO;
		}		

		@Override
		public Page<ProductDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Product> page = ProductRepo.findAll(pageRequest);
			
			Page<ProductDTO> page2 =  page.map(Product -> new ModelMapper().map(Product, ProductDTO.class));
			
			return page2;
		}

		@Override
		public Page<ProductDTO> searchByName(SearchDTO searchDTO) {
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			String name = searchDTO.getKeyword() != null  ? "" : searchDTO.getKeyword();

			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Product> page = ProductRepo.searchByName("%" + name + "%", pageRequest);
			
			Page<ProductDTO> page2 = page.map(product -> new ModelMapper().map(product, ProductDTO.class));
			
			return page2;
			
		}		
	}
}


