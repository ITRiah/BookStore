package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.CategoryDTO;
import com.example.BookStore.dto.CategoryDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Category;
import com.example.BookStore.repo.CategoryRepo;

public interface CategoryService {
	public void create(CategoryDTO categoryDTO);
	public Page<CategoryDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class CategoryServiceImpl implements CategoryService{
		
		@Autowired
		CategoryRepo CategoryRepo;

		@Override
		public void create(CategoryDTO CategoryDTO) {
			Category Category = new ModelMapper().map(CategoryDTO, Category.class);
			CategoryRepo.save(Category);
		}

		@Override
		public Page<CategoryDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Category> page = CategoryRepo.findAll(pageRequest);
			
			Page<CategoryDTO> page2 =  page.map(Category -> new ModelMapper().map(Category, CategoryDTO.class));
			
			return page2;
		}
		
	}
}


