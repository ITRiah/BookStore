package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.ProductDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Product;
import com.example.BookStore.entity.Product;
import com.example.BookStore.repo.ProductRepo;

public interface ProductService {
	public void create(ProductDTO ProductDTO);
	public Page<ProductDTO> getAll(SearchDTO searchDTO);
	public void updateAvatar(String fileName, int id);
	
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
		public void updateAvatar(String fileName, int id) {
			Product Product = ProductRepo.getById(id);
			Product.setImage(fileName);
			ProductRepo.save(Product);
		}
		
	}
}


