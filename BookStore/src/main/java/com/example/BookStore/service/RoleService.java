package com.example.BookStore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.BookStore.dto.RoleDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.entity.Role;
import com.example.BookStore.repo.RoleRepo;

public interface RoleService {
	public void create(RoleDTO roleDTO);
	public Page<RoleDTO> getAll(SearchDTO searchDTO);
	
	@Service
	public class RoleServiceImpl implements RoleService{
		
		@Autowired
		RoleRepo roleRepo;

		@Override
		public void create(RoleDTO roleDTO) {
			Role role = new ModelMapper().map(roleDTO, Role.class);
			roleRepo.save(role);
		}

		@Override
		public Page<RoleDTO> getAll(SearchDTO searchDTO) {
			
			int currentPage = searchDTO.getCurrentPage() == null ? 0 : searchDTO.getCurrentPage()  ;
			int size = searchDTO.getSize() == null ? 5 : searchDTO.getSize();
			
			String sortField = searchDTO.getSortedField() == null ? "id" : searchDTO.getSortedField();
			Sort sort = Sort.by(sortField).ascending();
			
			PageRequest pageRequest = PageRequest.of(currentPage, size, sort);
			Page<Role> page = roleRepo.findAll(pageRequest);
			
			Page<RoleDTO> page2 =  page.map(role -> new ModelMapper().map(role, RoleDTO.class));
			
			return page2;
		}
		
	}
}


